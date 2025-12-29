package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;

import java.io.IOException;

@WebServlet(name = "RemoveCategory", value = "/removeCategory")
public class RemoveCategory extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryId = request.getParameter("categoryId");

        if (categoryId != null) {
            int id = Integer.parseInt(categoryId);
            AdminDao adminDao = new AdminDao();
            adminDao.deleteCategory(id);
        }

        // Chuyển hướng về trang danh mục sau khi xóa
        response.sendRedirect("adminCategory");
    }
}
