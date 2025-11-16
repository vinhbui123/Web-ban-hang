package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.role;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CategoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class RoleValidation implements Filter {

    private final CategoryDao categoryDao = new CategoryDao();

    // Tắt chế độ TEST (BỎ QUA toàn bộ kiểm tra admin)
    private static final boolean TEST_MODE = false; // Đã đổi thành FALSE

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Lấy session, nếu chưa có thì tạo mới (true) hoặc không (false). Dùng false là đủ
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 1. Bỏ qua tài nguyên tĩnh và các trang công khai (login, register)
        if (uri.endsWith("login.jsp") || uri.endsWith("register.jsp")
                || uri.contains("/api/") || uri.contains("/css/")
                || uri.contains("/js/") || uri.contains("/images/")
                || uri.contains("/fonts/") || uri.endsWith(".png")
                || uri.endsWith(".jpg") || uri.endsWith(".gif")
                || uri.endsWith(".svg") || uri.endsWith(".ico")
        ) {
            chain.doFilter(request, response);
            return;
        }

        if (TEST_MODE) {
            // Trong môi trường thực tế, phần này phải được loại bỏ hoàn toàn.
            // Nếu bạn giữ lại, hãy đảm bảo rằng TEST_MODE LUÔN LÀ FALSE khi deploy.

            // Xử lý logic TEST_MODE như đã định nghĩa ban đầu (có thể bỏ qua nếu đã tắt TEST_MODE)
            if (session == null || session.getAttribute("user") == null) {
                User dummy = new User();
                dummy.setId(0);
                dummy.setRole(0); // Giả lập quyền Admin (nếu cần kiểm tra admin trong TEST_MODE)
                if (session == null) session = req.getSession(true);
                session.setAttribute("user", dummy);
            }
            if (session.getAttribute("category") == null) {
                List<Category> categories = categoryDao.getAll();
                session.setAttribute("category", categories);
            }
            chain.doFilter(request, response);
            return;
        }

        // ==========================================
        //  CODE ORIGINAL (chạy khi TEST_MODE = false)
        // ==========================================

        // Đảm bảo session được tạo nếu chưa có để lưu Category nếu cần
        if (session == null) session = req.getSession(true);
        User sessionUser = (User) session.getAttribute("user");

        // Load category (chỉ load một lần)
        if (session.getAttribute("category") == null) {
            List<Category> categories = categoryDao.getAll();
            session.setAttribute("category", categories);
        }

        // 2. Kiểm tra truy cập trang Admin
        // Chỉ cần kiểm tra nếu URI chứa "/admin" (hoặc thư mục admin của bạn)
        if (uri.contains("/admin")) {

//            // 2.1. Kiểm tra đăng nhập
//            if (sessionUser == null) {
//                // Chưa đăng nhập -> Chuyển hướng đến trang login
//                resp.sendRedirect(contextPath + "/login");
//                return;
//            }

            // 2.2. Kiểm tra trạng thái và cập nhật user
            User freshUser = new UserService().getById(sessionUser.getId());

            if (freshUser == null || freshUser.getStatus() == 0) {
                // User không tồn tại hoặc bị khóa/vô hiệu hóa (Status = 0)
                session.invalidate(); // Xóa session cũ
                resp.sendRedirect(contextPath + "/login"); // Chuyển hướng đến trang login
                return;
            }

            // Cập nhật session user (để lấy thông tin mới nhất và status)
            session.setAttribute("user", freshUser);

            // 2.3. Kiểm tra query Admin (Giả sử Role 1 là Admin)
            if (freshUser.getRole() != 1) { // Thay đổi điều kiện này tùy theo logic phân quyền của bạn
                // Không có quyền Admin -> Chuyển hướng đến trang lỗi 403 hoặc trang chủ
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này."); // Mã lỗi 403
                // Hoặc: resp.sendRedirect(contextPath + "/home");
                return;
            }
        }

        // 3. Cho phép truy cập (đối với trang thường hoặc trang admin đã thỏa mãn điều kiện)
        chain.doFilter(request, response);
    }
}