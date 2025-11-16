package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Order;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {

    // Lấy danh sách tất cả đơn hàng theo uid
    public List<Order> getAllOrders(int uid) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, uid);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setStatus(rs.getByte("status"));
                o.setUserId(rs.getInt("user_id"));
                o.setFreeShipping(rs.getInt("free_shipping"));
                o.setPaymentTypeId(rs.getInt("payment_type_id"));
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Thêm đơn hàng và chi tiết vào database (KHÔNG dùng total_amount nữa)
    public void addOrder(Order order, List<OrderDetail> details) {
        String query = "INSERT INTO orders (status, user_id, free_shipping, payment_type_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnect.getConnection()) { // chỉ mở 1 connection
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, order.getStatus());
                statement.setInt(2, order.getUserId());
                statement.setInt(3, order.getFreeShipping());
                statement.setInt(4, order.getPaymentTypeId());
                statement.executeUpdate();

                // Lấy order_id vừa tạo
                ResultSet generatedKeys = statement.getGeneratedKeys();
                int orderId = -1;
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Không lấy được order_id!");
                }

                // Thêm các chi tiết đơn hàng
                addDetailsOrder(connection, details, orderId, order.getStatus());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDetailsOrder(Connection connection, List<OrderDetail> details, int orderId, int status) {
        String query = "INSERT INTO order_details(order_id, product_id, price, quantity, total_money, discount_percentage, discount_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (OrderDetail detail : details) {
                statement.setInt(1, orderId);
                statement.setInt(2, detail.getProductId());
                statement.setInt(3, detail.getPrice());
                statement.setInt(4, detail.getQuantity());
                statement.setInt(5, detail.getTotalMoney());
                statement.setInt(6, detail.getDiscountPercentage());
                statement.setInt(7, detail.getDiscountAmount());
                statement.setInt(8, status);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAllOrdersForAdmin() {
        List<Map<String, Object>> result = new ArrayList<>();

        String query = """
            SELECT 
                o.id AS order_id,
                u.username,
                p.id AS product_id,
                p.name AS product_name,
                od.quantity,
                od.total_money,
                o.status,
                o.create_at,
                o.updated_at,
                pt.payment_name AS payment_method,
                pt.payment_code AS payment_code
               FROM orders o
            JOIN users u ON o.user_id = u.id
            JOIN order_details od ON o.id = od.order_id
            JOIN products p ON od.product_id = p.id
            JOIN payment_types pt ON o.payment_type_id = pt.id
            ORDER BY o.id DESC
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("order_id", rs.getInt("order_id"));
                row.put("username", rs.getString("username"));
                row.put("product_id", rs.getInt("product_id"));
                row.put("product_name", rs.getString("product_name"));
                row.put("quantity", rs.getInt("quantity"));
                row.put("total_money", rs.getInt("total_money"));
                row.put("status", rs.getByte("status"));
                row.put("create_at", rs.getTimestamp("create_at"));
                row.put("updated_at", rs.getTimestamp("updated_at"));
                row.put("payment_method", rs.getString("payment_method"));
                row.put("payment_code", rs.getString("payment_code"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public boolean cancelOrder(int orderId, int userId) {
        String updateOrderSql = "UPDATE orders SET status = 4 WHERE id = ? AND status IN (0, 1)";
        String selectDetailsSql = "SELECT product_id, quantity FROM order_details WHERE order_id = ?";

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Bước 1: Lấy danh sách sản phẩm trong đơn
            Map<Integer, Integer> productMap = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(selectDetailsSql)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");
                        productMap.put(productId, quantity);
                    }
                }
            }

            // Bước 2: Cập nhật trạng thái đơn hàng
            try (PreparedStatement ps = conn.prepareStatement(updateOrderSql)) {
                ps.setInt(1, orderId);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false; // không cập nhật được trạng thái
                }
            }

            // Bước 3: Hoàn lại kho và ghi log hủy từng sản phẩm
            InventoryDao inventoryDao = new InventoryDao();
            for (Map.Entry<Integer, Integer> entry : productMap.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                boolean stockOk = inventoryDao.updateInventory(conn, productId, quantity, 0);
                boolean transOk = inventoryDao.insertTransaction(conn, productId, userId, quantity, "cancel");

                if (!stockOk || !transOk) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); // tất cả OK
            return true;

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
        return false;
    }

    public boolean confirmOrder(int orderId) {
        // Chỉ cho phép xác nhận nếu đơn đang ở trạng thái 0
        String sql = "UPDATE orders SET status = 1, updated_at = NOW() WHERE id = ? AND status = 0";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
