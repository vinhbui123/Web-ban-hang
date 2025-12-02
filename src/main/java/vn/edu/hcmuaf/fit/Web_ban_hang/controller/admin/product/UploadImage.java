package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/images/*")
public class UploadImage extends HttpServlet {
    private String uploadDir;

    @Override
    public void init() throws ServletException {
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream("image.properties")) {
//            Properties prop = new Properties();
//            prop.load(input);
//            uploadDir = prop.getProperty("upload.image.path");
//        } catch (IOException e) {
//            throw new ServletException("Không đọc được cấu hình ảnh từ image.properties", e);
//        }
        // Lấy đường dẫn thật tới thư mục /images trong webapp
        uploadDir = getServletContext().getRealPath("/images");
        if (uploadDir == null) {
            throw new ServletException("Không tìm thấy thư mục /images trong webapp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getPathInfo().substring(1);
        File file = new File(uploadDir, filename);

        if (file.exists()) {
            response.setContentType(getServletContext().getMimeType(file.getName()));
            response.setContentLengthLong(file.length());

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

