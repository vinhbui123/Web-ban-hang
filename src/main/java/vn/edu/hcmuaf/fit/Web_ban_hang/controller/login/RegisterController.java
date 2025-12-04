//package vn.edu.hcmuaf.fit.Web_ban_hang.controller.login;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import vn.edu.hcmuaf.fit.Web_ban_hang.dao.model.Role;
//import vn.edu.hcmuaf.fit.Web_ban_hang.dao.model.User;
//import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//
//@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
//public class RegisterController extends HttpServlet {
//    private UserService userService;
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        userService = new UserService();
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String firstName = request.getParameter("firstName");
//        String lastName = request.getParameter("lastName");
//        String username = request.getParameter("username");
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String confirmPassword = request.getParameter("confirmPassword");
//        String avatar = request.getParameter("avatar");
//        String birthdayStr = request.getParameter("birthday");
//        String phoneNumber = request.getParameter("phoneNumber");
//        String address = request.getParameter("address");
//        String bio = request.getParameter("bio");
//
//        LocalDate birthday = null;
//        if (birthdayStr != null && !birthdayStr.isEmpty()) {
//            try {
//                birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            } catch (DateTimeParseException e) {
//                // Nếu người dùng nhập sai định dạng
//                String errorMessage = "Ngày sinh không đúng định dạng (yyyy-MM-dd).";
//                request.setAttribute("error", errorMessage);
//                request.setAttribute("firstName", firstName);
//                request.setAttribute("lastName", lastName);
//                request.setAttribute("username", username);
//                request.setAttribute("email", email);
//                request.setAttribute("password", password);
//                request.setAttribute("confirmPassword", confirmPassword);
//                request.setAttribute("avatar", avatar);
//                request.setAttribute("phoneNumber",phoneNumber);
//                request.setAttribute("address",address);
//                request.setAttribute("bio",bio);
//
//                request.getRequestDispatcher("register.jsp").forward(request, response);
//                return;
//            }
//        }
//
//        // Kiểm tra các trường nhập vào
//        String errorMessage = validateInputs(firstName, lastName, username, email, password, confirmPassword);
//
//        if (!errorMessage.isEmpty()) {
//            request.setAttribute("error", errorMessage);
//            request.setAttribute("firstName", firstName);
//            request.setAttribute("lastName", lastName);
//            request.setAttribute("username", username);
//            request.setAttribute("email", email);
//            request.setAttribute("password", password);
//            request.setAttribute("confirmPassword", confirmPassword);
//            request.setAttribute("avatar", avatar);
//            request.setAttribute("phoneNumber",phoneNumber);
//            request.setAttribute("address",address);
//            request.setAttribute("bio",bio);
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//            return;
//        }
//
//        // Kiểm tra email đã tồn tại chưa
//        if (userService.isEmailExists(email)) {
//            errorMessage = "Email đã tồn tại.";
//            request.setAttribute("error", errorMessage);
//            request.setAttribute("firstName", firstName);
//            request.setAttribute("lastName", lastName);
//            request.setAttribute("username", username);
//            request.setAttribute("email", email);
//            request.setAttribute("password", password);
//            request.setAttribute("confirmPassword", confirmPassword);
//            request.setAttribute("avatar", avatar);
//            request.setAttribute("phoneNumber",phoneNumber);
//            request.setAttribute("address",address);
//            request.setAttribute("bio",bio);
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//            return;
//        }
//
//        // Kiểm tra tên người dùng đã tồn tại chưa
//        if (userService.isUsernameExists(username)) {
//            errorMessage = "Tên người dùng đã tồn tại.";
//            request.setAttribute("error", errorMessage);
//            request.setAttribute("firstName", firstName);
//            request.setAttribute("lastName", lastName);
//            request.setAttribute("username", username);
//            request.setAttribute("email", email);
//            request.setAttribute("password", password);
//            request.setAttribute("confirmPassword", confirmPassword);
//            request.setAttribute("avatar", avatar);
//            request.setAttribute("phoneNumber",phoneNumber);
//            request.setAttribute("address",address);
//            request.setAttribute("bio",bio);
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//            return;
//        }
//
//        // Tạo đối tượng User và lưu vào cơ sở dữ liệu
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setAvatar(avatar);
//        user.setBirthday(birthday);
//        user.setEmail(email);
//        user.setPhoneNumber(phoneNumber);
//        user.setAddress(address);
//        Role role = new Role();
//        role.setId(2); // Giả sử role "User" có id = 2
//        user.setRole(role);
//        user.setStatus(1);
//        user.setBio(bio);
//        // kiểm tra trùng lặp username hoặc email
//        boolean success = userService.registerUser(user);
//        System.out.println(success);
//        if (success) {
//            request.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập.");
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//        } else {
//            errorMessage = "Email hoặc username đã tồn tại!";
//            request.setAttribute("error", errorMessage);
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//        }
//    }
//
//
//    private String validateInputs(String firstName, String lastName, String username, String email, String password, String confirmPassword) {
//        if (firstName == null || firstName.isEmpty()) return "Tên không được để trống.";
//        if (lastName == null || lastName.isEmpty()) return "Họ không được để trống.";
//        if (username == null || username.isEmpty()) return "Tên người dùng không được để trống.";
//        if (email == null || email.isEmpty()) return "Email không được để trống.";
//        if (!email.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7}$")) {
//            return "Email không hợp lệ.";
//        }
//        if (password == null || password.isEmpty()) return "Mật khẩu không được để trống.";
//        if (!password.equals(confirmPassword)) return "Mật khẩu và xác nhận mật khẩu không khớp.";
//        return "";
//    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        request.getRequestDispatcher("register.jsp").forward(request, response);
//    }
//}