package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Role;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.HashUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    // 1. Kiểm tra email đã tồn tại (không phân biệt hoa thường)
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(email) = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Kiểm tra username đã tồn tại
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Đăng ký user mới
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, avatar, birthday, email, phone_number, address, role, status, bio, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, HashUtil.toSHA256(user.getPassword()));
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getAvatar());
            stmt.setDate(6, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);
            stmt.setString(7, user.getEmail());
            stmt.setString(8, user.getPhoneNumber());
            stmt.setString(9, user.getAddress());
            Role defaultUserRole = new Role();
            defaultUserRole.setId(2); // id tương ứng với "User" trong DB

            user.setRole(defaultUserRole);
            stmt.setInt(10, user.getRole().getId());
            stmt.setInt(11, user.getStatus());
            stmt.setString(12, user.getBio());

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(13, now);
            stmt.setTimestamp(14, now);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật thông tin user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ?, bio = ? WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getBio());
            stmt.setString(7, user.getUsername());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tạo token reset password
    public boolean createResetToken(String email, String token, Timestamp expiryTime) {
        String sql = "INSERT INTO password_reset_tokens (email, token, expiry_time) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setTimestamp(3, expiryTime);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Validate token
    public Optional<String> getEmailByValidToken(String token) {
        String sql = "SELECT email FROM password_reset_tokens WHERE token = ? AND expiry_time > NOW()";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(rs.getString("email"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // 7. Update password theo email và gỡ auth_provider (cho phép đăng nhập bằng form)
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE users SET password = ?, auth_provider = NULL WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, HashUtil.toSHA256(newPassword));
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 8. Xóa token sau khi dùng
    public void deleteToken(String token) {
        String sql = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT u.id, u.username, u.email, u.status FROM users u ";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Tạo đối tượng Role
                Role role = new Role();
                role.setId(rs.getInt("role"));

                // Tạo đối tượng User
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getInt("status"));
                user.setRole(role);

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Check password hiện tại
    public boolean checkPassword(String username, String currentPassword) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(HashUtil.toSHA256(currentPassword));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 11. Cập nhật password mới
    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, HashUtil.toSHA256(newPassword));
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 12. Xác thực đăng nhập (đã bổ sung setPhoneNumber)
    public User authenticateUser(String username, String hashedPassword) {
        String sql = "SELECT *" + "FROM users WHERE username = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
//                String authProvider = rs.getString("auth_provider");
                int status = rs.getInt("status");

                // Log kiểm tra
                System.out.println("Username: " + username);
                System.out.println("🗄Hash trong DB: " + storedPassword);
                System.out.println("Hash từ service: " + hashedPassword);
                System.out.println("status: " + status);

                if (storedPassword.equals(hashedPassword)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setStatus(status);
                    Role role = new Role(rs.getInt("role"));

                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setAddress(rs.getString("address"));
                    user.setBio(rs.getString("bio"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setRole(role);
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update role & status
    public boolean updateUserRoleAndStatus(int userId, int newRole, int newStatus) {
        String sql = "UPDATE users SET role = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newRole);
            stmt.setInt(2, newStatus);
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Role role;
            if (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("role"));

                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getInt("status"));
                user.setRole(role);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 18. Lấy user theo username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users  WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            Role role;
            if (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("role"));

                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setStatus(rs.getInt("status"));
                user.setAddress(rs.getString("address"));
                user.setBio(rs.getString("bio"));
                user.setAvatar(rs.getString("avatar"));
                user.setRole(role);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
