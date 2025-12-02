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
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.utils.ReadJsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@WebServlet(urlPatterns = {"/add-cart"})
public class Add extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

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
//      System.out.println(jsonData);
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
        Integer quantity;
        Integer productId;
        try {
            productId = Integer.parseInt(String.valueOf(data.get("productId")));
            quantity = data.get("quantity") != null ? Integer.parseInt(data.get("quantity")) : 1;
        } catch (NumberFormatException e) {
            out.print("{ \"status\": false, \"message\": \" Lỗi id sản phẩm. \" }");
            out.flush();
            out.close();
            return;
        }

        // Thêm sản phẩm vào giỏ hàng (session/database)
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        try {
            if (cart.addByID(productId, quantity)) {
                out.print("{ \"status\": true, \"cartSize\": " + cart.getList().size() + " }");
            } else {
                out.print("{ \"status\": false, \"message\": \" Hết số lượng tồn kho! \"}");
            }
        } catch (Exception e) {
            out.print("{ \"status\": false, \"message\": \" Lỗi khi thêm sản phẩm! \" }");
        } finally {
            out.flush();
            out.close();
        }

    }
}