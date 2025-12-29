package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.OrderDao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/adminOrders")
public class OrderManagement extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, Object>> orders = orderDao.getAllOrdersForAdmin();
        request.setAttribute("orderDetails", orders);

        System.out.println("Order list retrieved: " + orders.size());
        request.getRequestDispatcher("ad-orders.jsp").forward(request, response);
    }
}
