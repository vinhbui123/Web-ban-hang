package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.order;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.OrderDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.OrderDTO;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Order;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.OrderDetail;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.PurchaseService;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/purchase")
public class PurchaseController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Giả sử user là một đối tượng và có phương thức getId để lấy ID của người dùng
            PurchaseService purchaseService = new PurchaseService();

            List<OrderDTO> orders = purchaseService.getAllPurchaseByUserID(user.getId());
            System.out.println(orders);
            request.setAttribute("orders", orders);  // thay vì PurchaseItems
            request.getRequestDispatcher("purchase.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(" doPost đã được gọi!");

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int price = Integer.parseInt(request.getParameter("price"));
            int total = quantity * price;

            // Tạo đơn hàng
            Order order = new Order();
            order.setStatus(0);
            order.setUserId(user.getId());
            order.setFreeShipping(1);
            order.setPaymentTypeId(1);

            OrderDetail detail = new OrderDetail();
            detail.setProductId(productId);
            detail.setPrice(price);
            detail.setQuantity(quantity);
            detail.setTotalMoney(total);
            detail.setDiscountPercentage(0);
            detail.setDiscountAmount(0);

            List<OrderDetail> details = List.of(detail);

            // 1. Kiểm tra tồn kho trước
            InventoryDao inventoryDao = new InventoryDao();
            int stock = inventoryDao.getStock(productId);
            if (stock < quantity) {
                request.setAttribute("error", " Không đủ hàng trong kho!");
                request.getRequestDispatcher("/purchase.jsp").forward(request, response);
                return;
            }

            // 2. Lưu đơn hàng nếu đủ hàng
            OrderDao orderDAO = new OrderDao();
            orderDAO.addOrder(order, details);
            System.out.println(" Đã thêm đơn hàng!");

            // 3. Xuất kho
            boolean success = inventoryDao.exportProduct(productId, quantity, user.getId(),"Đặt hàng");
            System.out.println(" export result: " + success);
            if (!success) {
                request.setAttribute("error", " Xuất kho thất bại sau khi tạo đơn hàng!");
                request.getRequestDispatcher("/purchase.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("purchase");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", " Lỗi khi đặt hàng");
            request.getRequestDispatcher("/purchase.jsp").forward(request, response);
        }
    }


}