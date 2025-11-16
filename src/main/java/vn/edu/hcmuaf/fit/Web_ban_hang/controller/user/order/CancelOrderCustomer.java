package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.OrderService;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/cancel-order-customer"})
public class CancelOrderCustomer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        PrintWriter out = response.getWriter();

        if (user == null || user.getUsername() == null) {
            out.print("{ \"status\": false, \"message\": \" Bạn cần đăng nhập để thực hiện thao tác này. \" }");
            out.flush();
            out.close();
            return;
        }

        String jsonData = ReadJsonUtil.read(request);
        int orderId = 0;
        try {
            orderId = new Gson().fromJson(jsonData, JsonObject.class).get("orderId").getAsInt();
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Mã đơn hàng không hợp lệ\"}");
        }
        OrderService orderService = new OrderService();

        boolean result = orderService.cancelOrder(orderId, user.getId());
        if (result) {
            out.print("{\"success\": true, \"message\": \"Hủy đơn hàng thành công\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Không thể hủy đơn hàng. Trạng thái hiện tại không cho phép.\"}");
        }
        out.flush();
        out.close();
    }
}
