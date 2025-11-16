package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.MaterialDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CategoryService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Show", value = "/adminProduct")
public class Show extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryService categoryService = new CategoryService();
        List<Category> categories = categoryService.getAll();
        request.setAttribute("category", categories);

        String categoryIdStr = request.getParameter("category");
        ProductService productService = new ProductService();
        List<Product> products;
        String categoryName = "Tất cả sản phẩm";

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                products = productService.getByCategory(categoryId); // đã có gắn material
                categoryName = categoryService.getCategoryNameById(categoryId);
            } catch (NumberFormatException e) {
                products = productService.getAll(); // đã có gắn material
            }
        } else {
            products = productService.getAll(); // đã có gắn material
        }
        MaterialDao materialDao = new MaterialDao();
        List<Material> materials = materialDao.getAll();
        request.setAttribute("materials", materials);

        request.setAttribute("products", products);
        request.setAttribute("selectedCategory", categoryName);
        request.getRequestDispatcher("ad-product.jsp").forward(request, response);
    }
}
