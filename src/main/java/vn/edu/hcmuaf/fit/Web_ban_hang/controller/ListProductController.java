package vn.edu.hcmuaf.fit.Web_ban_hang.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CategoryService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ListProductController", value = "/list-product")
public class ListProductController extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get all categories for filter dropdown
        List<Category> categories = categoryService.getAll();
        request.setAttribute("categories", categories);

        // Get filter parameters
        String categoryParam = request.getParameter("category");
        String minPriceParam = request.getParameter("minPrice");
        String maxPriceParam = request.getParameter("maxPrice");
        String typeParam = request.getParameter("type");

        List<Product> products;
        String categoryName = "Tất cả sản phẩm";

        // Get products based on filters
        if (typeParam != null) {
            if ("top-viewed".equals(typeParam)) {
                products = productService.getProductViewest(20);
                categoryName = "Xem nhiều nhất";
            } else if ("top-selling".equals(typeParam)) {
                products = productService.getTopRatedProducts();
                categoryName = "Sản phẩm nổi bật";
            } else {
                products = productService.getAll();
            }
        } else if (categoryParam != null && !"all".equals(categoryParam)) {
            try {
                int categoryId = Integer.parseInt(categoryParam);
                products = productService.getByCategory(categoryId);
                // Find category name
                for (Category cat : categories) {
                    if (cat.getId() == categoryId) {
                        categoryName = cat.getName();
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                products = productService.getAll();
            }
        } else {
            products = productService.getAll();
        }

        // Apply price filters
        if (minPriceParam != null && !minPriceParam.isEmpty()) {
            try {
                int minPrice = Integer.parseInt(minPriceParam);
                products = products.stream()
                        .filter(p -> p.getPrice() >= minPrice)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
            }
        }

        if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
            try {
                int maxPrice = Integer.parseInt(maxPriceParam);
                products = products.stream()
                        .filter(p -> p.getPrice() <= maxPrice)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
            }
        }

        request.setAttribute("products", products);
        request.setAttribute("categoryName", categoryName);
        request.getRequestDispatcher("list-product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
