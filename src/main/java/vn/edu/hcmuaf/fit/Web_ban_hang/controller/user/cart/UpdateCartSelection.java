package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.cart;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.session.Cart;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "UpdateCartSelection", value = "/update-cart-selection")
public class UpdateCartSelection extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        PrintWriter out = response.getWriter();

        if (cart == null) {
            out.print("{\"status\": false, \"message\": \"Giỏ hàng không tồn tại\"}");
            return;
        }

        String jsonData = ReadJsonUtil.read(request);
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(jsonData, new TypeToken<Map<String, Object>>() {
        }.getType());

        int productId = Integer.parseInt(data.get("productId").toString());
        boolean selected = Boolean.parseBoolean(data.get("selected").toString());

        // Cập nhật trạng thái chọn của sản phẩm trong giỏ hàng
        cart.getList().stream()
                .filter(item -> item.getId() == productId)
                .findFirst()
                .ifPresent(item -> item.setSelected(selected));

        session.setAttribute("cart", cart);
        out.print("{\"status\": true}");
        out.flush();
        out.close();
    }
} 