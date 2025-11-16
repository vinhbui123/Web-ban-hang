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

@WebServlet(urlPatterns = {"/update-cart"})
public class Update extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        PrintWriter out = response.getWriter();

        if (cart == null) {
            out.print("{ \"status\": false, \"message\": \" cart trống! \" }");
            out.flush();
            out.close();
            return;
        }
        String jsonData = ReadJsonUtil.read(request);
//      System.out.println(jsonData);
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());

        try {
            int productId = Integer.parseInt(String.valueOf(data.get("productId")));
            int newQuantity = Integer.parseInt(String.valueOf(data.get("newQuantity")));

            cart.update(productId, newQuantity);
            session.setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            out.print("{ \"status\": false, \"message\": \" Lỗi dữ liệu đầu vào! \" }");
        }
        out.print("{ \"status\": true , \"message\": \" Thành công \" }");
        out.flush();
        out.close();
    }
}