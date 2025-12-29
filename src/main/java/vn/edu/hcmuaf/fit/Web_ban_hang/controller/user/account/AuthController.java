package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CartService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;

import java.io.IOException;

// 1. Update urlPatterns to include both login and logout
@WebServlet(name = "AuthController", urlPatterns = {"/login", "/logout"})
public class AuthController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        // 2. Check which URL triggered this method
        if ("/logout".equals(path)) {
            handleLogout(request, response);
        } else {
            // Default to showing the login page
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Post is usually only for the Login form submission
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);

        // --- Authenticate User ---
        UserService userService = new UserService();
        session.removeAttribute("user"); // Clear previous user if any

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            // Set session attributes
            if (session.getAttribute("cart") == null) {
                session.setAttribute("cart", new CartService());
            }
            session.setAttribute("user", user);
            log.info("Đăng nhập thành công: {}", user.getUsername());
            response.sendRedirect(request.getContextPath() + ("/home"));
            return;
        }
        request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng");
        request.setAttribute("username", username);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    // Helper method to handle logout logic
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // Remove session form client
        }
    // Redirect to home (using context path for safety)
        response.sendRedirect(request.getContextPath() + "/home");
    }
}