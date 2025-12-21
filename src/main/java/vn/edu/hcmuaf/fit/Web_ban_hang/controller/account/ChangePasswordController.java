package vn.edu.hcmuaf.fit.Web_ban_hang.controller.account;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String currentPassword = request.getParameter("currentPassword") != null ? request.getParameter("currentPassword") : "";
        String newPassword = request.getParameter("newPassword") != null ? request.getParameter("newPassword") : "";
        String confirmPassword = request.getParameter("confirmPassword") != null ? request.getParameter("confirmPassword") : "";

        // Kiểm tra nhập đầy đủ
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới xác nhận
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu
        boolean isUpdated = userService.updatePassword(user.getUsername(), newPassword);
        if (isUpdated) {
            user.setPassword(newPassword); // Cập nhật session
            session.setAttribute("user", user);
            request.setAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
        }

        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

//    // Hàm kiểm tra mật khẩu mạnh
//    private boolean isStrongPassword(String password) {
//        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$");
//    }
}
