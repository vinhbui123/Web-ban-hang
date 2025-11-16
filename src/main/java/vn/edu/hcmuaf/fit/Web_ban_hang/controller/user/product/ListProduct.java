package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.MaterialDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.CategoryService;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ListProduct", value = {"/list-product"})
public class ListProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách danh mục
        CategoryService categoryService = new CategoryService();
        List<Category> categories = categoryService.getAll();
        req.setAttribute("category", categories);

        // Lấy danh sách chất liệu
        MaterialDao materialDao = new MaterialDao();
        List<Material> materials = materialDao.getAll();
        req.setAttribute("materials", materials);

        // Lấy các tham số lọc
        String categoryIdParam = req.getParameter("category");
        String materialIdParam = req.getParameter("material");
        String inStockParam = req.getParameter("inStock");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");

        String typeParam = req.getParameter("type");

        ProductService productService = new ProductService();
        List<Product> products;
        String categoryName = "TẤT CẢ SẢN PHẨM";

        if ("top-viewed".equals(typeParam)) {
            products = productService.getProductsViewedAbove(200);// lấy sản phẩm xem nhiều trên 200 lượt xem
            categoryName = "Sản phẩm xem nhiều nhất 👁️";
        } else if ("top-selling".equals(typeParam)) {
            products = productService.getTopRatedProducts(); //  Lấy sản phẩm nổi bật dựa trên rating
            categoryName = "Sản phẩm nổi bật 🔥";
        } else {


        products = productService.getAll();

        // Lọc theo tồn kho nếu người dùng chọn
        if ("true".equals(inStockParam)) {
            products = products.stream()
                    .filter(p -> p.getStock() > 0)
                    .collect(Collectors.toList());
        }

        // Lọc theo danh mục
        if (categoryIdParam != null && !categoryIdParam.isEmpty() && !categoryIdParam.equals("all")) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam);
                categoryName = categoryService.getById(categoryId).getName();
                products = products.stream()
                        .filter(p -> p.getCatalog_id() == categoryId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }

        if (minPriceParam != null && !minPriceParam.isEmpty() || maxPriceParam != null && !maxPriceParam.isEmpty()) {
            try {
                final int minPrice = (minPriceParam != null && !minPriceParam.isEmpty()) 
                    ? Integer.parseInt(minPriceParam) : 0;
                final int maxPrice = (maxPriceParam != null && !maxPriceParam.isEmpty()) 
                    ? Integer.parseInt(maxPriceParam) : Integer.MAX_VALUE;

                products = products.stream()
                        .filter(p -> {
                            int finalPrice = p.getPrice();
                            if (p.getDiscount() > 0) {
                                finalPrice = finalPrice - (finalPrice * p.getDiscount() / 100);
                            }
                            return finalPrice >= minPrice && finalPrice <= maxPrice;
                        })
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
                // Nếu giá không hợp lệ, bỏ qua việc lọc theo giá
            }
        }
        }

        req.setAttribute("categoryName", categoryName);
        req.setAttribute("products", products);
        req.getRequestDispatcher("list-product.jsp").forward(req, resp);
    }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

}

