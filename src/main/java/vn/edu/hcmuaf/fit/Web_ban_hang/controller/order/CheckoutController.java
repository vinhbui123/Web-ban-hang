package vn.edu.hcmuaf.fit.Web_ban_hang.controller.order;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto.OrderDTO;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Address;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Order;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.OrderDetail;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CartService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.AddressService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.OrderService;
import java.io.IOException;
import java.io.PrintWriter;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;

import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;
import java.util.List;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // 1. Parse JSON
            String jsonInput = ReadJsonUtil.read(request);
            // System.out.println(jsonInput);

            Gson gson = new Gson();
            OrderDTO orderDTO = gson.fromJson(jsonInput, OrderDTO.class);

            OrderService orderService = new OrderService();
            Order order = new Order(orderDTO.getStatus(), orderDTO.getUserId(), orderDTO.getFreeShipping(),
                    orderDTO.getPaymentTypeId());
            List<OrderDetail> details = orderService.toDetailOrder(orderDTO.getDetails());

            // 2. Kiểm tra tồn kho
            InventoryDao inventoryDao = new InventoryDao();
            for (OrderDetail detail : details) {
                int stock = inventoryDao.getStock(detail.getProductId());
                if (stock < detail.getQuantity()) {
                    out.print("{\"success\": false, \"message\": \"Không đủ hàng trong kho cho SP ID: "
                            + detail.getProductId() + "\"}");
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
                System.out.println("Exporting productId=" + detail.getProductId() + ", quantity="
                        + detail.getQuantity() + ", userId=" + orderDTO.getUserId());

                boolean success = inventoryDao.exportProduct(detail.getProductId(), detail.getQuantity());
                if (!success) {
                    out.print("{\"success\": false, \"message\": \"Trừ kho thất bại sau khi đã lưu đơn.\"}");
                    return;
                }
            }
            CartService cart = (CartService) session.getAttribute("cart");
            for (OrderDetail detail : details) {
                cart.remove(detail.getProductId());
            }

            out.print("{\"success\": true}");

        } catch (Exception e) {
            log.error(e.getMessage());
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // Nếu bạn dùng GET để hiển thị trang checkout
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object cart = session.getAttribute("cart");
        User user = (User) session.getAttribute("user");

        if (user == null || user.getUsername() == null) {
            request.setAttribute("message", "Cần đăng nhập để thực hiện thao tác này.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (cart == null || ((CartService) cart).getList().isEmpty()) {
            request.setAttribute("isCartEmpty", true);
            request.setAttribute("message", "Giỏ hàng của bạn đang trống.");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }
        if (session.getAttribute("addressDefault") == null) {
            AddressService addressService = new AddressService();
            Address defaultAddress = addressService.getAddressDefault(user.getId());
            session.setAttribute("addressDefault", defaultAddress);
        }
        // Truyền lại thông tin người nhận
        request.setAttribute("cart", cart);
        request.setAttribute("isCartEmpty", false);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}
