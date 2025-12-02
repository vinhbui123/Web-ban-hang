package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.HashUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    /**
     * Helper method to map ResultSet to User object.
     * Reduces code duplication in getAllUsers, getById, authenticateUser, etc.
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setStatus(rs.getInt("status"));
//        user.setAuthProvider(rs.getString("auth_provider"));
        user.setAddress(rs.getString("address"));
        user.setBio(rs.getString("bio"));
        user.setAvatar(rs.getString("avatar"));

        // Map fields that might be null or specific to certain queries if needed
        // (Assuming your Join always returns role data

        user.setRole(rs.getInt("role"));
        return user;
    }

    // Helper to check if column exists in ResultSet to avoid Exceptions
    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnLabel(x))) {
                return true;
            }
        }
        return false;
    }

    // 1. Check email exists
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(email) = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Error checking email: ", e);
        }
        return false;
    }

    // 2. Check username exists
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Error checking username: ", e);
        }
        return false;
    }

    // 3. Register new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, avatar, birthday, email, phone_number, address, role, status, bio, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, HashUtil.toSHA256(user.getPassword())); // Hash immediately
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getAvatar());
            stmt.setDate(6, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);
            stmt.setString(7, user.getEmail());
            stmt.setString(8, user.getPhoneNumber());
            stmt.setString(9, user.getAddress());

            // Ensure Role is set
            int roleId = user.getRole(); // Default to User
            stmt.setInt(10, roleId);

            stmt.setInt(11, user.getStatus());
            stmt.setString(12, user.getBio());

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(13, now);
            stmt.setTimestamp(14, now);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error registering user: ", e);
        }
        return false;
    }

    // 4. Update user info
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
            log.error("Error updating user: ", e);
        }
        return false;
    }

    // 5. Create reset token
    public boolean createResetToken(String email, String token, Timestamp expiryTime) {
        String sql = "INSERT INTO password_reset_tokens (email, token, expiry_time) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setTimestamp(3, expiryTime);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error creating reset token: ", e);
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
            log.error("Error validating token: ", e);
        }
        return Optional.empty();
    }

    // 7. Update password by email
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE users SET password = ?, auth_provider = NULL WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, HashUtil.toSHA256(newPassword));
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error updating password by email: ", e);
        }
        return false;
    }

    // 8. Delete token
    public void deleteToken(String token) {
        String sql = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting token: ", e);
        }
    }

    // 9. Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            log.error("Error getting all users: ", e);
        }
        return users;
    }

    // 10. Check current password
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
            log.error("Error checking password: ", e);
        }
        return false;
    }

    // 11. Update password
    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, HashUtil.toSHA256(newPassword));
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error updating password: ", e);
        }
        return false;
    }

    // 12. Authenticate User
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // Compare Stored Hash vs Hashed Input
                if (storedPassword != null && storedPassword.equals(password)) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            log.error("Error validating user: ", e);
        }
        return null;
    }

    // 13. Update role & status
    public boolean updateUserRoleAndStatus(int userId, int newRole, int newStatus) {
        String sql = "UPDATE users SET role = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newRole);
            stmt.setInt(2, newStatus);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error updating role/status: ", e);
        }
        return false;
    }

    // 14. Get user by Email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting user by email: ", e);
        }
        return null;
    }

    // Unified method for Social Login Insert
    private boolean insertSocialUser(User user) {
        String sql = "INSERT INTO users (email, username, first_name, last_name, role, status, password, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String randomPassword = UUID.randomUUID().toString();
            // Hash the random password just in case they try to login via form later (though they won't know it)
            String hashedPassword = HashUtil.toSHA256(randomPassword);

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());

            int roleId = user.getRole() != 0 ? user.getRole() : 0;
            stmt.setInt(5, roleId);
            stmt.setInt(6, user.getStatus());
            stmt.setString(8, hashedPassword);
            stmt.setTimestamp(9, now);
            stmt.setTimestamp(10, now);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error inserting social user: ", e);
        }
        return false;
    }

    // 18. Get user by ID
    public User getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting user by ID: ", e);
        }
        return null;
    }

    // 19. Get user by Username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Error getting user by username: ", e);
        }
        return null;
    }

    // 20. Session Management
    public String getLoggedSessionId(int userId) {
        String sql = "SELECT logged_session_id FROM users WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("logged_session_id");
            }
        } catch (SQLException e) {
            log.error("Error getting session ID: ", e);
        }
        return null;
    }

    public void updateSessionId(int userId, String sessionId) {
        String sql = "UPDATE users SET logged_session_id = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating session ID: ", e);
        }
    }
}