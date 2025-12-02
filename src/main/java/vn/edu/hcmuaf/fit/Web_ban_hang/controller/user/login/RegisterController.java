package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.login;

import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import java.io.IOException;

@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Quan trọng: Xử lý tiếng Việt cho dữ liệu nhập vào
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu từ form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String avatar = request.getParameter("avatar");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String bio = request.getParameter("bio");

        // 2. Kiểm tra tính hợp lệ của input (Basic Validation)
        String errorMessage = userService.validateInputs(firstName, lastName, username, email, password, confirmPassword);

        // trả về giá trì sai in ra màn hình
        if (errorMessage != null) {
            handleRegisterError(request, response, errorMessage);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setBio(bio);
        user.setRole(0);
        user.setStatus(1);

        // 5. Gọi Service để lưu
        boolean success = userService.registerUser(user);

        if (success) {
            // Đăng ký thành công -> Chuyển sang trang login
            request.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Lỗi hệ thống hoặc lỗi không xác định
            handleRegisterError(request, response, "Đăng ký thất bại. Vui lòng thử lại sau.");
        }
    }

    private void handleRegisterError(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        request.setAttribute("error", error);

        // Refill form: Giữ lại thông tin để người dùng đỡ phải nhập lại
        request.setAttribute("firstName", request.getParameter("firstName"));
        request.setAttribute("lastName", request.getParameter("lastName"));
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
        request.setAttribute("address", request.getParameter("address"));
        request.setAttribute("bio", request.getParameter("bio"));
        request.setAttribute("avatar", request.getParameter("avatar"));

        // Lưu ý: KHÔNG bao giờ set lại password và confirmPassword vì lý do bảo mật

        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}