package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.UserDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.HashUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDao userDao = new UserDao();

    // Mã hóa mật khẩu
    private String hashPassword(String password) {
        return HashUtil.toSHA256(password);
    }

    // Kiểm tra email
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

    // Tạo token đặt lại mật khẩu
    public Optional<String> generateResetToken(String email) {
        if (email == null) return Optional.empty();

        email = email.trim().toLowerCase();

        System.out.println("Checking email: [" + email + "]");

        if (!isEmailExists(email)) {
            System.out.println("Email không tồn tại trong DB!");
            return Optional.empty();
        }

        System.out.println("Email tồn tại. Tạo token...");

        String token = UUID.randomUUID().toString();
        Timestamp expiryTime = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));
        boolean success = userDao.createResetToken(email, token, expiryTime);
        return success ? Optional.of(token) : Optional.empty();
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

    // Xác thực đăng nhập
    public User authenticateUser(String username, String password) {
        String hashed = hashPassword(password); // Mã hóa SHA-256
        return userDao.authenticateUser(username, hashed); // truyền hash vào DAO
    }

    // Cập nhật role và status người dùng
    public boolean updateUserRoleAndStatus(int userId, int newRole, int newStatus) {
        return userDao.updateUserRoleAndStatus(userId, newRole, newStatus);
    }

    public User getById(int id){
        return new UserDao().getById(id);
    }

}
