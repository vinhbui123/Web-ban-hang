package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.ProductDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.io.File;
import java.io.IOException;

@WebServlet(name = "Remove", value = "/adminRemove")
public class Remove extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdParam = request.getParameter("productId");

        if (productIdParam != null && !productIdParam.isEmpty()) {
            try {
                int productId = Integer.parseInt(productIdParam);

                // Lấy thông tin sản phẩm\
                ProductDao productDao = new ProductDao();
                Product product = productDao.getById(productId);
                if (product != null) {
                    String imagePath = product.getImg(); // "images/moc_cao.jpg"

                    // 🗑 Xóa ảnh trong thư mục webapp/images nếu tồn tại
                    if (imagePath != null && !imagePath.isEmpty()) {
                        String fullImagePath = getServletContext().getRealPath("/" + imagePath.replace("/", File.separator));
                        File imageFile = new File(fullImagePath);
                        if (imageFile.exists() && imageFile.isFile()) {
                            imageFile.delete();
                        }
                    }

                    // Xóa sản phẩm trong DB
                    boolean result = new AdminDao().removeProduct(productId);

                    if (result) {
                        request.getSession().setAttribute("message", "Xóa sản phẩm thành công.");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Lỗi khi xóa sản phẩm.");
                        request.getSession().setAttribute("messageType", "error");
                    }
                } else {
                    request.getSession().setAttribute("message", "Không tìm thấy sản phẩm.");
                    request.getSession().setAttribute("messageType", "error");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("message", "ID sản phẩm không hợp lệ.");
                request.getSession().setAttribute("messageType", "error");
            }
        } else {
            request.getSession().setAttribute("message", "Thiếu ID sản phẩm.");
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/adminProducts");
    }
}