package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDao {

    // ✅ Kiểm tra tồn kho hiện tại
    public int getStock(int productId) {
        String sql = "SELECT quantity FROM inventory WHERE product_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("quantity");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public boolean updateInventory(Connection conn, int productId, int quantityIn, int quantityOut) {
        String sql = """
                INSERT INTO inventory (product_id, quantity_in, quantity_out)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    quantity_in = quantity_in + VALUES(quantity_in),
                    quantity_out = quantity_out + VALUES(quantity_out)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, quantityIn);
            stmt.setInt(3, quantityOut);

            int rows = stmt.executeUpdate();
            System.out.println("📝 updateInventory: " + (rows > 0));
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ updateInventory lỗi:");
            e.printStackTrace();
        }

        return false;
    }


    // ✅ Lưu lịch sử giao dịch - dùng Connection truyền vào để dùng chung transaction
    public boolean insertTransaction(Connection conn, int productId, int userId, int quantity, String type) {
        String sql = """
                    INSERT INTO inventory_transactions (product_id, user_id, quantity, type, created_at)
                    VALUES (?, ?, ?, ?, NOW())
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, userId);
            stmt.setInt(3, quantity);
            stmt.setString(4, type);

            int rows = stmt.executeUpdate();
            System.out.println("🧾 insertTransaction: " + (rows > 0));
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ insertTransaction lỗi:");
            e.printStackTrace();
        }
        return false;
    }


    public boolean importProduct(int productId, int quantity, int userId) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // bật transaction

            boolean transOk = insertTransaction(conn, productId, userId, quantity, "import");
            boolean updateOk = updateInventory(conn, productId, quantity, 0);

            System.out.println("📦 updateInventory: " + updateOk);

            if (!transOk || !updateOk) {
                System.out.println("❌ Một trong hai thao tác thất bại!");
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean exportProduct(int productId, int quantity, int userId, String type) {
        Connection conn = null;
        try {
            int stock = getStock(productId);
            if (stock < quantity) {
                System.out.println("⚠️ Không đủ hàng trong kho!");
                return false;
            }

            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            boolean transOk = insertTransaction(conn, productId, userId, quantity, type);
            boolean updateOk = updateInventory(conn, productId, 0, quantity);

            if (!transOk || !updateOk) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
        return false;
    }



    // ✅ Lấy lịch sử giao dịch (tái sử dụng)
    public List<Map<String, Object>> getTransactionHistory() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
                    SELECT t.id, p.name AS product_name, t.type, t.quantity, 
                           u.username, u.first_name, u.last_name, u.role, t.created_at
                    FROM inventory_transactions t
                    JOIN products p ON t.product_id = p.id
                    JOIN users u ON t.user_id = u.id
                    ORDER BY t.created_at DESC
                """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("product_name", rs.getString("product_name"));
                map.put("type", rs.getString("type"));
                map.put("quantity", rs.getInt("quantity"));
                map.put("username", rs.getString("username"));
                map.put("name", rs.getString("first_name") + " " + rs.getString("last_name"));
                map.put("role", rs.getInt("role"));
                map.put("created_at", rs.getTimestamp("created_at"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
