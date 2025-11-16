package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.statistics;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;

import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/admin")
public class AdminDashboard extends HttpServlet {
    private final AdminDao adminDao=new AdminDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> stats = adminDao.getAdminOrderStatistics();
        Map<String, Integer> userStats = adminDao.getUserStatistics();

        req.setAttribute("stats", stats);
        req.setAttribute("userStats", userStats);
        req.getRequestDispatcher("ad-dashboard.jsp").forward(req, resp);
    }
}