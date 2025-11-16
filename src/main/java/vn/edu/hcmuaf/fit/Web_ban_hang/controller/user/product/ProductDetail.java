package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CommentDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Comment;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetail", value = "/product-detail")
public class ProductDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pid = request.getParameter("id");
        if (pid != null) {
            int id = Integer.parseInt(pid);
            ProductService productService = new ProductService();
            productService.increaseView(id);
            Product product = productService.getById(id);
            if (product != null) {
                InventoryDao inventoryDao = new InventoryDao();
                product.setStock(inventoryDao.getStock(product.getId()));
                List<Product> products = productService.getRelatetByCategory(product.getCatalog_id(), id, 5);
                CommentDao commentDao = new CommentDao();
                List<Comment> comments = commentDao.getCommentsByProductId(id);
                double avgRating = commentDao.getAverageRatingByProductId(id);

                System.out.println("Tổng số comment: " + comments.size());
                for (Comment c : comments) {
                    System.out.println(" -> " + c.getUserName() + ": " + c.getContent());
                }

                request.setAttribute("comments", comments);
                request.setAttribute("averageRating", avgRating);
                request.setAttribute("commentCount", comments.size());
                request.setAttribute("product", product);
                request.setAttribute("products", products);
                request.getRequestDispatcher("product-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("layoutProduct.jsp");
            }
        } else {
            response.sendRedirect("layoutProduct.jsp");
        }
    }
}