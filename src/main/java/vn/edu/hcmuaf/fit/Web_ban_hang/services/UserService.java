package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.login.RegisterController;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.UserDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.HashUtil;

import java.util.*;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao = new UserDao();

    // Xác thực đăng nhập
    public User authenticateUser(String username, String password) {
        String hashed = HashUtil.toSHA256(password); // mã hóa SHA-256
        return userDao.authenticateUser(username, hashed); // truyền hash vào DAO
    }

    //input filter
    public String validateInputs(String firstName, String lastName, String username, String email, String password, String confirmPassword) {
        if (firstName == null || firstName.isEmpty()) return "Tên không được để trống.";
        if (lastName == null || lastName.isEmpty()) return "Họ không được để trống.";
        if (username == null || username.isEmpty()) return "Tên người dùng không được để trống.";
        if (email == null || email.isEmpty()) return "Email không được để trống.";
        if (!email.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7}$")) {
            return "Email không hợp lệ.";
        }
        if (password == null || password.isEmpty()) return "Mật khẩu không được để trống.";
        if (!password.equals(confirmPassword)) return "Mật khẩu và xác nhận mật khẩu không khớp.";
        if (isEmailExists(email)) return "Email đã được sử dụng";
        if (isUsernameExists(username)) return "Tên đăng nhập đã tồn tại.";
        return null;
    }

    // Add this method to your UserService.java class
    public String validateUpdateProfile(String firstName, String lastName, String newEmail, String currentEmail) {
        if (firstName == null || firstName.isEmpty()) return "Tên không được để trống.";
        if (lastName == null || lastName.isEmpty()) return "Họ không được để trống.";
        if (newEmail == null || newEmail.isEmpty()) return "Email không được để trống.";

        // Check email format
        if (!newEmail.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7}$")) {
            return "Email không hợp lệ.";
        }

        // ONLY check if email exists if the user is changing it
        if (!newEmail.equals(currentEmail) && isEmailExists(newEmail)) {
            return "Email đã được sử dụng bởi tài khoản khác.";
        }

        return null;
    }

    // Kiểm tra email có tồn tại
    public boolean isEmailExists(String email) {
        return userDao.isEmailExists(email);
    }

    // Kiểm tra username
    public boolean isUsernameExists(String username) {
        return userDao.isUsernameExists(username);
    }

    // Đăng ký người dùng
    public boolean registerUser(User user) {
        if (isUsernameExists(user.getUsername()) || isEmailExists(user.getEmail())) {
            return false;
        }
        return userDao.registerUser(user);
    }

    // Cập nhật thông tin người dùng
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }

    // Xác thực token
    public Optional<String> validateToken(String token) {
        return userDao.getEmailByValidToken(token);
    }

    // Đặt lại mật khẩu
    public boolean resetPassword(String token, String newPassword) {
        Optional<String> emailOpt = validateToken(token);
        if (emailOpt.isPresent()) {
            String email = emailOpt.get();
            boolean updated = userDao.updatePasswordByEmail(email, newPassword);
            if (updated) {
                userDao.deleteToken(token);
                return true;
            }
        }
        return false;
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    // Kiểm tra mật khẩu hiện tại
    public boolean checkPassword(String username, String currentPassword) {
        return userDao.checkPassword(username, currentPassword);
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String username, String newPassword) {
        return userDao.updatePassword(username, newPassword);
    }

    // Cập nhật role và status người dùng
    public boolean updateUserRoleAndStatus(int userId, int newRole, int newStatus) {
        return userDao.updateUserRoleAndStatus(userId, newRole, newStatus);
    }

    public User getById(int id) {
        return new UserDao().getById(id);
    }

    // Lấy sessionId hiện đang lưu trong DB
    public String getLoggedSessionId(int userId) {
        return userDao.getLoggedSessionId(userId);
    }

//    // Cập nhật sessionId vào DB sau khi đăng nhập
//    public void updateLoggedSessionId(int userId, String sessionId) {
//        userDao.updateSessionId(userId, sessionId);
//    }
}
