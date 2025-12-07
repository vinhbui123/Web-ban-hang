package vn.edu.hcmuaf.fit.Web_ban_hang.controller.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import vn.edu.hcmuaf.fit.Web_ban_hang.controller.cart.Cart;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;

import java.io.IOException;
import java.time.Instant;

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);

        UserService userService = new UserService();
        User user = userService.authenticateUser(username, password);

        if (user != null) {
//            session.setAttribute("cart", new Cart());
            session.setAttribute("user", user);

            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") == null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
