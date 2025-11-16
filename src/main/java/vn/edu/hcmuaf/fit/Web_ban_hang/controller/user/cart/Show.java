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

@WebServlet(name = "ShowCart",value="/cart")
public class Show extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        cart.setStock();

        session.setAttribute("totalMoney", cart.getTotal());
        session.setAttribute("totalQuantityAll",cart.getTotalQuantityAll());

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        PrintWriter out = response.getWriter();
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String jsonData = ReadJsonUtil.read(request);
        System.out.println(jsonData);
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
        if(data.get("action").equals("clearAll")){
            cart.removeAll();
            out.print("{ \"status\": true}");
            session.setAttribute("cart",cart);
        } else {
            out.print("{ \"status\": false}");
        }
        session.setAttribute("totalMoney", cart.getTotal());
        session.setAttribute("totalQuantityAll",cart.getTotalQuantityAll());
        out.flush();
        out.close();
    }
}
