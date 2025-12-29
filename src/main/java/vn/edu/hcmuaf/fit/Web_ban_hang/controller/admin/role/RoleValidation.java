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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 1. Bỏ qua tài nguyên tĩnh và các trang công khai (login, register)
        // Để tránh vòng lặp redirect vô hạn hoặc chặn file css/js
        if (uri.endsWith("login.jsp") || uri.endsWith("register.jsp")
                || uri.contains("/login") || uri.contains("/register") // Thêm trường hợp mapping URL không đuôi .jsp
                || uri.contains("/api/") || uri.contains("/css/")
                || uri.contains("/js/") || uri.contains("/images/")
                || uri.contains("/fonts/") || uri.endsWith(".png")
                || uri.endsWith(".jpg") || uri.endsWith(".gif")
                || uri.endsWith(".svg") || uri.endsWith(".ico")
        ) {
            chain.doFilter(request, response);
            return;
        }

        // Tạo session nếu chưa có để lưu Category hoặc lấy User
        HttpSession session = req.getSession(true);
        User sessionUser = (User) session.getAttribute("user");

        // 2. Load Global Data (Ví dụ: Danh mục sản phẩm cho Menu)
        // Logic này giữ lại vì cần hiển thị Category ở mọi trang
        if (session.getAttribute("category") == null) {
            List<Category> categories = categoryDao.getAll();
            session.setAttribute("category", categories);
        }

        // 3. Kiểm tra bảo mật cho trang Admin
        if (uri.contains("/admin")) {

            // 3.1. Kiểm tra đăng nhập (session có tồn tại User không?)
            if (sessionUser == null) {
                // Chưa đăng nhập -> Chuyển hướng về trang Login
                resp.sendRedirect(contextPath + "/login.jsp"); // Hoặc đường dẫn mapping "/login"
                return;
            }

            // 3.2. Kiểm tra trạng thái thực tế từ Database (đề phòng User bị khóa trong lúc đang đăng nhập)
            UserService userService = new UserService();
            User freshUser = userService.getById(sessionUser.getId());

            if (freshUser == null || freshUser.getStatus() == 0) {
                // User không tồn tại hoặc bị khóa (Status = 0) -> Hủy session và đá về Login
                session.invalidate();
                resp.sendRedirect(contextPath + "/login.jsp");
                return;
            }

            // Cập nhật lại thông tin user mới nhất vào session
            session.setAttribute("user", freshUser);

            // 3.3. Kiểm tra quyền (Role)
            // Giả sử: 0 = User thường, 1 = Admin (hoặc > 0 là Admin tùy quy ước)
            if (freshUser.getRole() != 1) {
                // Đã đăng nhập nhưng không phải Admin -> Báo lỗi 403 Forbidden
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang quản trị.");
                return;
            }
        }

        // 4. Cho phép request đi tiếp (vào trang chủ, trang sản phẩm, hoặc trang admin nếu đã qua các bước kiểm tra trên)
        chain.doFilter(request, response);
    }
}