package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CategoryService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCategory", value = "/adminCategory")
public class AddCategory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách danh mục từ database
        CategoryService categoryService = new CategoryService();
        List<Category> categories = categoryService.getAll();
        System.out.println("Danh sách danh mục: " + categories);


        // Đưa danh sách danh mục vào request để hiển thị trên trang JSP
        request.setAttribute("category", categories);
        request.getRequestDispatcher("ad-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryName = request.getParameter("categoryName");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục không được để trống!");
        } else {
            // Thêm danh mục vào database
            Category category = new Category();
            category.setName(categoryName);

            AdminDao adminDao = new AdminDao();
            adminDao.addCategory(category);
        }

        // Cập nhật danh sách danh mục sau khi thêm
        doGet(request, response);
    }
}
