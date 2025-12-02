package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import vn.edu.hcmuaf.fit.Web_ban_hang.services.UserService;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "AccountController", urlPatterns = "/account")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB buffer
        maxFileSize = 1024 * 1024 * 5,       // 5MB max file size
        maxRequestSize = 1024 * 1024 * 10    // 10MB max request size
)
public class AccountController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Xử lý họ và tên
        String fullName = request.getParameter("fullName");
        String[] parts = fullName.trim().split(" ", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(request.getParameter("email"));
        user.setPhoneNumber(request.getParameter("phoneNumber"));
        user.setAddress(request.getParameter("address"));
        user.setBio(request.getParameter("bio"));

        //  Xử lý ảnh đại diện
        Part avatarPart = request.getPart("avatarUpload");
        if (avatarPart != null && avatarPart.getSize() > 0) {
            String originalFileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
            String newFileName = System.currentTimeMillis() + "_" + originalFileName;

            String uploadPath = request.getServletContext().getRealPath("/images/avatars");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String filePath = uploadPath + File.separator + newFileName;
            avatarPart.write(filePath);

            user .setAvatar("images/avatars/" + newFileName);
        }

        // Cập nhật DBF
        UserService userService = new UserService();
        boolean success = userService.updateUser(user );

        if (!success) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin người dùng.");
            return;
        }
        session.setAttribute("currentUser", user );
        request.setAttribute("successMessage", "Thông tin của bạn đã được cập nhật thành công.");
        request.getRequestDispatcher("account.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("account.jsp").forward(request, response);
    }
}
