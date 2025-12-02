package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.user;

import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManagerUsers", value = "/adminUsers")
public class ManagerUsers extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);

        // Đảm bảo thông báo lỗi được giữ nếu có
        String error = (String) request.getAttribute("errorMessage");
        if (error != null) {
            request.setAttribute("errorMessage", error);
        }
        request.getRequestDispatcher("ad-customer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            int userId = Integer.parseInt(request.getParameter("customerId"));
            int newRole = Integer.parseInt(request.getParameter("customerRole"));
            int newStatus = Integer.parseInt(request.getParameter("customerStatus"));

            boolean updated = userService.updateUserRoleAndStatus(userId, newRole, newStatus);

            if (updated) {
                // Sử dụng session để lưu thông báo
                session.setAttribute("message", "Cập nhật thành công!");
                if(userId == user.getId()){
                    User updatedUser=userService.getById(userId);
                    session.setAttribute("user",updatedUser);
                    System.out.println("Cập nhật lại user session: " + updatedUser.getUsername() + " - Role ID: " + updatedUser.getRole());
                }
            } else {
                session.setAttribute("message", "Cập nhật không thành công!");
            }

            response.sendRedirect(request.getContextPath() + "/adminUsers");

        } catch (NumberFormatException e) {
            session.setAttribute("message", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/adminUsers");
        }
    }


}