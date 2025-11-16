package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.session.Cart;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;

import java.io.IOException;
import java.time.Instant;

// 1. Update urlPatterns to include both login and logout
@WebServlet(name = "LoginController", urlPatterns = {"/login", "/logout"})
public class AuthController extends HttpServlet {
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME = 5 * 60 * 1000; // 5 minutes
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

        // --- 1. Check if the account is currently locked ---
        Long lockTime = (Long) session.getAttribute("lockTime");
        Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");

        if (lockTime != null) {
            long currentTime = Instant.now().toEpochMilli();
            if (currentTime - lockTime < LOCK_TIME) {
                // Still locked
                request.setAttribute("errorMessage", "Bạn đã nhập sai quá 5 lần. Vui lòng thử lại sau 5 phút.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            } else {
                // Lock expired, reset counters
                session.removeAttribute("lockTime");
                session.removeAttribute("failedAttempts");
                failedAttempts = 0;
            }
        }

        // --- 2. Authenticate User ---
        UserService userService = new UserService();
        session.removeAttribute("user"); // Clear previous user if any

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            // Clear security counters on success
            session.removeAttribute("failedAttempts");
            session.removeAttribute("lockTime");

            // Handle Session ID updating
            String currentSessionId = session.getId();
            // userService.updateLoggedSessionId(user.getId(), currentSessionId);

            // Set session attributes
            session.setAttribute("cart", new Cart());
            session.setAttribute("user", user);
            log.info("Đăng nhập thành công: {}", user.getUsername());
            response.sendRedirect(request.getContextPath() + "/home");

        } else {
            // --- FAILURE CASE ---
            failedAttempts = (failedAttempts == null) ? 1 : failedAttempts + 1;
            session.setAttribute("failedAttempts", failedAttempts);

            if (failedAttempts >= MAX_ATTEMPTS) {
                // Lock the account
                session.setAttribute("lockTime", Instant.now().toEpochMilli());
                request.setAttribute("errorMessage", "Bạn đã nhập sai quá 5 lần. Vui lòng thử lại sau 5 phút.");
            } else {
                // Warning message
                int remaining = MAX_ATTEMPTS - failedAttempts;
                request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng. Bạn còn " + remaining + " lần thử.");
            }

            request.setAttribute("username", username);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Helper method to handle logout logic
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("user");

            // if (user != null) {
            //     // Xóa sessionId đang lưu trong DB
            //     new UserService().updateLoggedSessionId(user.getId(), null);
            // }
            session.invalidate(); // Xóa session phía client
        }

        // Redirect to home (using context path for safety)
        response.sendRedirect(request.getContextPath() + "/home");
    }
}