package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;

import java.sql.*;

public class InventoryDao {

    /**
     * Lấy số lượng tồn kho của sản phẩm
     * stock = quantity (hoặc quantity_in + quantity_returned - quantity_out -
     * quantity_damaged)
     */
    public int getStock(int productId) {
        String sql = "SELECT quantity FROM inventory WHERE product_id = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Cập nhật số lượng tồn kho (dùng khi checkout/order)
     */
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
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean exportProduct(int productId, int quantity, int userId, String transactionType) {
        String insertTransactionSql = "INSERT INTO inventory_transactions (product_id, user_id, quantity, transaction_type) VALUES (?, ?, ?, ?)";
        String updateInventorySql = "UPDATE inventory SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Ghi log giao dịch
                try (PreparedStatement ps = conn.prepareStatement(insertTransactionSql)) {
                    ps.setInt(1, productId);
                    ps.setInt(2, userId);
                    ps.setInt(3, quantity);
                    ps.setString(4, transactionType); // "export"
                    ps.executeUpdate();
                }

                // 2. Trừ kho
                try (PreparedStatement ps = conn.prepareStatement(updateInventorySql)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantity);
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Không đủ hàng trong kho");
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertTransaction(Connection conn, int productId, int userId, int quantity, String type) {
        String sql = "INSERT INTO inventory_transactions (product_id, user_id, quantity, transaction_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setInt(3, quantity);
            ps.setString(4, type);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
