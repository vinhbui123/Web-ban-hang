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
}
