package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.order;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.OrderDTO;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Order;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.OrderDetail;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.session.Cart;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.OrderService;
import java.io.IOException;
import java.io.PrintWriter;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;

import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;
import java.util.List;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // 1. Parse JSON
            String jsonInput = ReadJsonUtil.read(request);
//            System.out.println(jsonInput);
            Gson gson = new Gson();
            OrderDTO orderDTO = gson.fromJson(jsonInput, OrderDTO.class);

            OrderService orderService = new OrderService();
            Order order = new Order(orderDTO.getStatus(), orderDTO.getUserId(), orderDTO.getFreeShipping(), orderDTO.getPaymentTypeId());
            List<OrderDetail> details = orderService.toDetailOrder(orderDTO.getDetails());

            // 2. Kiểm tra tồn kho
            InventoryDao inventoryDao = new InventoryDao();
            for (OrderDetail detail : details) {
                int stock = inventoryDao.getStock(detail.getProductId());
                if (stock < detail.getQuantity()) {
                    out.print("{\"success\": false, \"message\": \"Không đủ hàng trong kho cho SP ID: " + detail.getProductId() + "\"}");
                    return;
                }
            }

            // 3. Kiểm tra địa chỉ giao hàng
            HttpSession session = request.getSession(false);
            Address address = (Address) session.getAttribute("addressDefault");
            if (address == null) {
                out.print("{\"success\": false, \"message\": \"Cập nhật địa chỉ đơn hàng trước khi đặt hàng.\"}");
                return;
            }

            // 4. Lưu đơn hàng
            orderService.addOrder(order, details);

            // 5. Trừ kho
            for (OrderDetail detail : details) {
                System.out.println("🔍 Exporting productId=" + detail.getProductId() + ", quantity=" + detail.getQuantity() + ", userId=" + orderDTO.getUserId());

                boolean success = inventoryDao.exportProduct(detail.getProductId(), detail.getQuantity(), orderDTO.getUserId(),"export");
                if (!success) {
                    out.print("{\"success\": false, \"message\": \"Trừ kho thất bại sau khi đã lưu đơn.\"}");
                    return;
                }
            }
            Cart cart = (Cart) session.getAttribute("cart");
            for(OrderDetail detail : details){
                cart.remove(detail.getProductId());
            }

            out.print("{\"success\": true}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }


    // Nếu bạn dùng GET để hiển thị trang checkout
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object cart = session.getAttribute("cart");
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUsername() == null) {
            request.setAttribute("message", "Cần đăng nhập để thực hiện thao tác này.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (cart == null || ((Cart) cart).getList().isEmpty()) {
            request.setAttribute("isCartEmpty", true);
            request.setAttribute("message", "Giỏ hàng của bạn đang trống.");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        // Truyền lại thông tin người nhận
        request.setAttribute("cart", cart);
        request.setAttribute("isCartEmpty", false);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}
