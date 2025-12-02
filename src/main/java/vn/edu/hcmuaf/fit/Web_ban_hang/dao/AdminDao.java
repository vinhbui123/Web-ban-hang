package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AdminDao {

    private static final Logger log = LoggerFactory.getLogger(AdminDao.class);

    public static void addProduct(Product product, int quantityIn, String[] materialIds) {
        String queryInsertProduct = "INSERT INTO products (name, price, discount, view, img, catalog_id, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String queryInsertTransaction = "INSERT INTO inventory_transactions (product_id, type, quantity) VALUES (?, 'import', ?)";
        String queryInsertInventory = "INSERT INTO inventory (product_id, quantity_in) VALUES (?, ?) ON DUPLICATE KEY UPDATE quantity_in = quantity_in + VALUES(quantity_in)";
        String queryInsertProductMaterial = "INSERT INTO product_materials (product_id, material_id) VALUES (?, ?)";

        try (Connection connection = DBConnect.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtProduct = connection.prepareStatement(queryInsertProduct, Statement.RETURN_GENERATED_KEYS)) {
                stmtProduct.setString(1, product.getName());
                stmtProduct.setInt(2, product.getPrice());
                stmtProduct.setInt(3, product.getDiscount());
                stmtProduct.setInt(4, product.getView());
                stmtProduct.setString(5, product.getImg());
                stmtProduct.setInt(6, product.getCatalog_id());
                stmtProduct.setString(7, product.getDescription());

                int affectedRows = stmtProduct.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = stmtProduct.getGeneratedKeys()) {
                        if (rs.next()) {
                            int productId = rs.getInt(1);

                            // Thêm vào inventory_transactions
                            try (PreparedStatement stmtTransaction = connection.prepareStatement(queryInsertTransaction)) {
                                stmtTransaction.setInt(1, productId);
                                stmtTransaction.setInt(2, quantityIn);
                                stmtTransaction.executeUpdate();
                            }

                            // Thêm vào inventory
                            try (PreparedStatement stmtInventory = connection.prepareStatement(queryInsertInventory)) {
                                stmtInventory.setInt(1, productId);
                                stmtInventory.setInt(2, quantityIn);
                                stmtInventory.executeUpdate();
                            }

                            // Thêm material cho sản phẩm
                            if (materialIds != null) {
                                try (PreparedStatement stmtMaterial = connection.prepareStatement(queryInsertProductMaterial)) {
                                    for (String mid : materialIds) {
                                        int materialId = Integer.parseInt(mid);
                                        stmtMaterial.setInt(1, productId);
                                        stmtMaterial.setInt(2, materialId);
                                        stmtMaterial.addBatch();
                                    }
                                    stmtMaterial.executeBatch();
                                }
                            }
                        }
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Cập nhật sản phẩm
    public static void updateProduct(Product product) {
        String query = "UPDATE products SET catalog_id = ?, name = ?, img = ?, price = ?, discount = ?, view = ?, description = ?, updated_at = ? WHERE id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, product.getCatalog_id());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getImg());
            stmt.setInt(4, product.getPrice());
            stmt.setInt(5, product.getDiscount());
            stmt.setInt(6, product.getView());
            stmt.setString(7, product.getDescription());
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Cập nhật thời gian hiện tại
            stmt.setInt(9, product.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật sản phẩm thành công!");
            } else {
                System.out.println("Không tìm thấy sản phẩm để cập nhật!");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    public boolean removeProduct(int productId) {
        boolean result = false;
        String query = "DELETE FROM products WHERE id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                result = true;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    // Thêm danh mục sản phẩm
    public void addCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ 2. Xóa danh mục
    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ 3. Chỉnh sửa danh mục
    public void updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateProductMaterials(int productId, String[] materialIds) {
        try (Connection conn = DBConnect.getConnection()) {
            // Xoá chất liệu cũ
            String deleteSQL = "DELETE FROM product_materials WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // Thêm chất liệu mới
            if (materialIds != null) {
                String insertSQL = "INSERT INTO product_materials (product_id, material_id) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    for (String mid : materialIds) {
                        ps.setInt(1, productId);
                        ps.setInt(2, Integer.parseInt(mid));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getAdminOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();

        String sql = """
        SELECT
            (SELECT COUNT(*) FROM orders) AS total_orders,
            (SELECT COUNT(*) FROM orders WHERE status = 0) AS pending_orders,
            (SELECT COUNT(*) FROM orders WHERE status = 1) AS confirmed_orders,
            (SELECT COUNT(*) FROM orders WHERE status = 3) AS done_orders,
            (SELECT COUNT(*) FROM orders WHERE status = 4) AS cancelled_orders,
            (
                SELECT SUM(od.total_money)
                FROM orders o
                JOIN order_details od ON o.id = od.order_id
                WHERE o.status = 3
            ) AS total_revenue
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("total_orders", rs.getInt("total_orders"));
                stats.put("pending_orders", rs.getInt("pending_orders"));
                stats.put("confirmed_orders", rs.getInt("confirmed_orders"));
                stats.put("done_orders", rs.getInt("done_orders"));
                stats.put("cancelled_orders", rs.getInt("cancelled_orders"));
                stats.put("total_revenue", rs.getInt("total_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
    public Map<String, Integer> getUserStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = """
        SELECT 
            COUNT(*) AS total_users,
            SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS locked_users
        FROM users
    """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                stats.put("total_users", rs.getInt("total_users"));
                stats.put("locked_users", rs.getInt("locked_users"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }


}
