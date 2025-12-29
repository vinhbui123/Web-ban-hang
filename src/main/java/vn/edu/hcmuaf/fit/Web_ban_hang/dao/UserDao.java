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
    private static UserDao instance;

    public static UserDao getInstance() {
        if (instance == null)
            instance = new UserDao();
        return instance;
    }

    // ============================================================
    // PRIVATE HELPERS (CORE ENGINE) - XỬ LÝ JDBC CHUNG
    // ============================================================

    // 1. Map dữ liệu từ ResultSet vào User Object
    private User mapUser(ResultSet rs) throws SQLException {
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
        user.setRole(rs.getInt("role"));
        // Thêm set password nếu cần thiết để verify
        user.setPassword(rs.getString("password"));
        return user;
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            for (int x = 1; x <= columns; x++) {
                if (columnName.equals(rsmd.getColumnLabel(x))) return true;
            }
        } catch (SQLException e) { return false; }
        return false;
    }

    // 2. Tự động gán tham số vào dấu ? (Hỗ trợ Date, Timestamp, Null)
    //sử dụng Object.. thay thế khỏi khai báo Object[] = {a,b,c}
    private void setParameters(PreparedStatement stmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            Object param = parameters[i];
            switch (param) {
                case null -> stmt.setNull(i + 1, Types.NULL);
                case java.util.Date date -> stmt.setDate(i + 1, new Date(date.getTime()));
                case LocalDateTime localDateTime -> stmt.setTimestamp(i + 1, Timestamp.valueOf(localDateTime));
                default -> stmt.setObject(i + 1, param);
            }
        }
    }

    // 3. Thực thi query Update/Insert/Delete (Trả về boolean)
    private boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Update Error: " + sql, e);
            return false;
        }
    }

    // 4. Thực thi query lấy 1 User (SELECT * trả về Object)
    private User queryOne(String sql, Object... params) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (SQLException e) {
            log.error("Query One Error: " + sql, e);
        }
        return null;
    }

    // 5. Thực thi query lấy danh sách User (SELECT * trả về List)
    private List<User> queryList(String sql, Object... params) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            log.error("Query List Error: " + sql, e);
        }
        return users;
    }

    // 6. Thực thi query lấy giá trị đơn (COUNT, lấy string cột cụ thể...)
    private <T> T queryScalar(String sql, Class<T> type, Object... params) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1, type);
                }
            }
        } catch (SQLException e) {
            log.error("Query Scalar Error: " + sql, e);
        }
        return null;
    }

    // ============================================================
    // PUBLIC METHODS - NGẮN GỌN & SẠCH SẼ
    // ============================================================

    // 1. Check exists
    public boolean isEmailExists(String email) {
        Long count = queryScalar("SELECT COUNT(*) FROM users WHERE email = ?", Long.class, email.trim().toLowerCase());
        return count != null && count > 0;
    }

    public boolean isUsernameExists(String username) {
        Long count = queryScalar("SELECT COUNT(*) FROM users WHERE username = ?", Long.class, username);
        return count != null && count > 0;
    }

    // 2. Get User Info
    public User getById(int id) {
        return queryOne("SELECT * FROM users WHERE id = ?", id);
    }

    public User getUserByUsername(String username) {
        return queryOne("SELECT * FROM users WHERE username = ?", username);
    }

    public List<User> getAllUsers() {
        return queryList("SELECT * FROM users");
    }

    // 3. Xác thực
    public User authenticateUser(String username, String currentPassword) {
        // Lấy user từ DB lên trước
        User user = getUserByUsername(username);

        // Kiểm tra null và so sánh hash
        if (user != null && user.getPassword() != null) {
            String inputHash = HashUtil.toSHA256(currentPassword);
            if (user.getPassword().equals(inputHash)) {
                return user; // Login thành công
            }
        }
        log.info("Authentication failed for: " + username);
        return null;
    }

    // 4. Create / Register
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, avatar, birthday, email, phone_number, address, role, status, bio, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        // set role mặc định là 0 nếu không có
        int roleId = user.getRole() != 0 ? user.getRole() : 0;

        return executeUpdate(sql,
                user.getUsername(),
                HashUtil.toSHA256(user.getPassword()), // Hash ngay lập tức
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getBirthday(), // Helper sẽ tự cast sang java.sql.Date
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                roleId,
                user.getStatus(),
                user.getBio(),
                now,
                now
        );
    }

    // Social Login Insert
    public boolean insertSocialUser(User user) {
        String sql = "INSERT INTO users (email, username, first_name, last_name, role, status, password, create_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String randomPassHash = HashUtil.toSHA256(UUID.randomUUID().toString());
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        return executeUpdate(sql,
                user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getRole(), user.getStatus(), randomPassHash, now, now
        );
    }

    // 5. Cập nhật thông tin người dùng
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, address = ?, bio = ?, avatar = ? WHERE username = ?";
        return executeUpdate(sql,
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber(), user.getAddress(), user.getBio(),user.getAvatar(),
                user.getUsername()
        );
    }

    public boolean updatePassword(String username, String newPassword) {
        return executeUpdate("UPDATE users SET password = ? WHERE username = ?",
                HashUtil.toSHA256(newPassword), username);
    }


    public boolean updateUserRoleAndStatus(int userId, int newRole, int newStatus) {
        return executeUpdate("UPDATE users SET role = ?, status = ? WHERE id = ?", newRole, newStatus, userId);
    }

    public String getLoggedSessionId(int userId) {
        return queryScalar("SELECT logged_session_id FROM users WHERE id = ?", String.class, userId);
    }

}