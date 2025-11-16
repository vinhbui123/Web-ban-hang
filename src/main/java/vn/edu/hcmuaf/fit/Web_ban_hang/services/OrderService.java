package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.OrderDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.DetailOrderDTO;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Order;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDao orderDao = new OrderDao();

    // Add order to DB
    public void addOrder(Order order, List<OrderDetail> orderDetails) {
        orderDao.addOrder(order, orderDetails);
    }

    // Get list order by uid
    public List<Order> getAllOrders(int userId) {
        return orderDao.getAllOrders(userId);
    }

    // detailsOrderDTO to detailOrders
    public List<OrderDetail> toDetailOrder(List<DetailOrderDTO> detailOrderDTOS) {
        List<OrderDetail> result = new ArrayList<>();
        for (DetailOrderDTO dto : detailOrderDTOS) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(dto.getProductId());
            orderDetail.setPrice(dto.getPrice());
            orderDetail.setQuantity(dto.getQuantity());
            orderDetail.setTotalMoney(dto.getPrice() * dto.getQuantity());  // ✅ THÊM DÒNG NÀY
            orderDetail.setDiscountAmount(0); // Nếu bạn chưa dùng chiết khấu
            orderDetail.setDiscountPercentage(0); // Nếu bạn chưa dùng phần trăm chiết khấu
            result.add(orderDetail);
        }
        return result;
    }

    public boolean cancelOrder(int orderId, int userId) {
        return orderDao.cancelOrder(orderId, userId);
    }



}
