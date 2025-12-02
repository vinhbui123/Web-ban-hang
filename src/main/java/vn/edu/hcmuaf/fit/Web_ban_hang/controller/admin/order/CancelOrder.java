package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.OrderDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import java.io.IOException;

@WebServlet(name = "CancelOrder", urlPatterns = "/cancelOrder")
public class CancelOrder extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdRaw = request.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdRaw);
            // ✅ Lấy user từ session
            HttpSession session = request.getSession(false);
            User currentUser = (User) session.getAttribute("user");

            if (currentUser != null) {
                boolean success = orderDao.cancelOrder(orderId, currentUser.getId());
                if (success) {
                    System.out.println("✅ Admin đã hủy đơn hàng #" + orderId);
                } else {
                    System.out.println("❌ Hủy đơn hàng thất bại");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/adminOrders");
    }
}
