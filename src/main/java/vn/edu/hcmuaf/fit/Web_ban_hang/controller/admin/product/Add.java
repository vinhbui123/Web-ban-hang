package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.AdminDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.MaterialDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "Add", value = "/adminAdd")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class Add extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String priceParam = request.getParameter("price");
        String quantityParam = request.getParameter("quantity");
        String description = request.getParameter("description");
        String catalogIdParam = request.getParameter("category");
        Part filePart = request.getPart("image"); // Lấy ảnh từ form


        try {
            int price = Integer.parseInt(priceParam);
            int quantity = Integer.parseInt(quantityParam);
            int catalogId = Integer.parseInt(catalogIdParam);

            // Lấy tên file
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String imgPath;

            if (!fileName.isEmpty()) {
                // Đường dẫn thư mục ảnh trong webapp/images
                String uploadPath = getServletContext().getRealPath("/images");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String filePath = uploadPath + File.separator + fileName;
                filePart.write(filePath);

                imgPath = "images/" + fileName;
            } else {
                imgPath = "images/default.jpg"; // Có thể thay bằng ảnh mặc định
            }

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDescription(description);
            product.setCatalog_id(catalogId);
            product.setImg(imgPath);

            AdminDao adminDao = new AdminDao();


            String[] materialIds = request.getParameterValues("materialIds");
            System.out.println("Tên sản phẩm: " + name);
            System.out.println("Số lượng: " + quantity);
            System.out.println("Chất liệu chọn: ");
            if (materialIds != null) {
                for (String id : materialIds) {
                    System.out.println(">> " + id);
                }
            } else {
                System.out.println(">> KHÔNG CHỌN");
            }

            AdminDao.addProduct(product, quantity, materialIds);
            System.out.println("Material IDs:");
            if (materialIds != null) {
                for (String id : materialIds) {
                    System.out.println(">> " + id);
                }
            } else {
                System.out.println(">> NULL");
            }
            MaterialDao materialDao = new MaterialDao();
            List<Material> materials = materialDao.getAll();
            request.setAttribute("materials", materials);
            // Đặt thông báo vào session để giữ khi redirect
            request.getSession().setAttribute("message", "✔️ Thêm sản phẩm thành công!");
            request.getSession().setAttribute("messageType", "success");
            response.sendRedirect(request.getContextPath() + "/adminProduct");

        } catch (NumberFormatException e) {
            response.getWriter().println("Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }

}
