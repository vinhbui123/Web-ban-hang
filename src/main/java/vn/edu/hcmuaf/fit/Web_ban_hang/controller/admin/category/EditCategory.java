package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;

import java.io.IOException;

@WebServlet(name = "EditCategory", value = "/editCategory")
public class EditCategory extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");

        if (categoryIdStr == null || categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục không được để trống!");
        } else {
            int categoryId = Integer.parseInt(categoryIdStr);

            // Cập nhật danh mục vào database
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);

            AdminDao adminDao = new AdminDao();
            adminDao.updateCategory(category);
        }

        // Sau khi cập nhật, load lại danh sách danh mục
        response.sendRedirect(request.getContextPath() + "/adminCategory");
    }
}
