package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.ProductDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "Edit", value = "/adminEdit")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class Edit extends HttpServlet {

    private String getUploadDir(HttpServletRequest request) {
        return getServletContext().getRealPath("/images");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdParam = request.getParameter("productId");
        String name = request.getParameter("name");
        String priceParam = request.getParameter("price");
        String description = request.getParameter("description");
        String catalogIdParam = request.getParameter("category");
        String oldImage = request.getParameter("oldImage");
        Part filePart = request.getPart("image");
        String[] materialIds = request.getParameterValues("materialIds");

        try {
            int productId = Integer.parseInt(productIdParam);
            int price = Integer.parseInt(priceParam);
            int catalogId = Integer.parseInt(catalogIdParam);

            Product existingProduct = ProductDao.getById(productId);
            if (existingProduct != null) {
                existingProduct.setName(name);
                existingProduct.setPrice(price);
                existingProduct.setDescription(description);
                existingProduct.setCatalog_id(catalogId);

                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                if (!fileName.isEmpty()) {
                    String uploadPath = getUploadDir(request);
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String filePath = uploadPath + File.separator + fileName;
                    filePart.write(filePath);
                    existingProduct.setImg("images/" + fileName);
                } else {
                    existingProduct.setImg(oldImage);
                }

                AdminDao.updateProduct(existingProduct);
                AdminDao.updateProductMaterials(existingProduct.getId(), materialIds);
                // Thông báo thành công
                request.getSession().setAttribute("message", "✔️ Cập nhật sản phẩm thành công!");
                request.getSession().setAttribute("messageType", "success");
            } else {
                // Thông báo không tìm thấy sản phẩm
                request.getSession().setAttribute("message", "❌ Không tìm thấy sản phẩm để cập nhật!");
                request.getSession().setAttribute("messageType", "error");
            }
        } catch (NumberFormatException e) {
            // Dữ liệu không hợp lệ
            request.getSession().setAttribute("message", "❌ Dữ liệu không hợp lệ. Vui lòng kiểm tra lại!");
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/adminProduct");
    }
}
