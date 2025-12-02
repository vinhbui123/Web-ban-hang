package vn.edu.hcmuaf.fit.Web_ban_hang.dao;


import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.OrderDTO;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.PurchaseItem;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.SettingDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PurchaseDao {

    public List<OrderDTO> getAllPurchaseByUser(int userId) {
        Map<Integer, OrderDTO> orderMap = new LinkedHashMap<>();

        String sql = """
                    SELECT\s
                        o.id AS order_id,
                        o.user_id,
                        o.status,
                        o.create_at,
                        o.updated_at,
                        o.free_shipping,
                        o.payment_type_id,
                       \s
                        od.id AS detail_id,
                        od.product_id,
                        od.price,
                        od.quantity,
                        od.total_money,
                        od.discount_percentage,
                        od.discount_amount,
                
                        p.name AS product_name,
                        p.img AS product_img
                    FROM orders o
                    JOIN order_details od ON o.id = od.order_id
                    JOIN products p ON od.product_id = p.id
                    WHERE o.user_id = ?
                    ORDER BY o.create_at DESC;
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                OrderDTO order = orderMap.get(orderId);
                if (order == null) {
                    order = new OrderDTO();
                    order.setId(orderId);
                    order.setUserId(userId);
                    order.setStatus(rs.getInt("status"));
                    order.setStatusString(SettingDAO.toStatusDetails(order.getStatus()));
                    order.setFreeShipping(rs.getInt("free_shipping"));
                    order.setCreatedAt(rs.getTimestamp("create_at"));
                    order.setPurchaseItems(new ArrayList<>());

                    orderMap.put(orderId, order);
                }

                PurchaseItem item = new PurchaseItem();
                item.setIdProduct(rs.getInt("product_id"));
                item.setIdDetail(rs.getInt("detail_id"));
                item.setImg(rs.getString("product_img"));
                item.setName(rs.getString("product_name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setDiscount(rs.getInt("discount_percentage"));
                item.setTotal(rs.getInt("total_money"));
                item.setStatus(order.getStatus()); // gán theo order
                item.setStatusString(order.getStatusString());

                order.getPurchaseItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(orderMap.values());
    }

}