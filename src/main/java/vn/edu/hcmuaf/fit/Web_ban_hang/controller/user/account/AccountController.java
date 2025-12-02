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
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class AccountController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        request.setCharacterEncoding("UTF-8"); // Ensure Vietnamese characters are read correctly

        // 1. Get Inputs
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        // Split Full Name into First/Last
        String[] parts = fullName.trim().split(" ", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        // 2. Validate using the NEW method (Pass current email to allow keeping it)
        String errorMsg = userService.validateUpdateProfile(firstName, lastName, fullName, email);

        if (errorMsg != null) {
            handleUpdateUserError(request, response, errorMsg);
            return;
        }

        // 3. Update User Object
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(request.getParameter("address"));
        user.setBio(request.getParameter("bio"));

        // 4. Handle Avatar Upload
        Part avatarPart = request.getPart("avatarUpload");
        if (avatarPart != null && avatarPart.getSize() > 0) {
            String originalFileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
            String newFileName = System.currentTimeMillis() + "_" + originalFileName;

            String uploadPath = request.getServletContext().getRealPath("/images/avatars");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String filePath = uploadPath + File.separator + newFileName;
            avatarPart.write(filePath);

            user.setAvatar("images/avatars/" + newFileName);
        }

        // 5. Update Database
        boolean success = this.userService.updateUser(user);

        if (!success) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin người dùng.");
            request.getRequestDispatcher("account.jsp").forward(request, response);
            return;
        }

        // 6. Update Session and Success
        session.setAttribute("user", user); // Ensure you update "user", not "currentUser" if JSP uses "user"
        request.setAttribute("successMessage", "Thông tin của bạn đã được cập nhật thành công.");
        request.getRequestDispatcher("account.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("account.jsp").forward(request, response);
    }

    private void handleUpdateUserError(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        // 1. Send the error message (Use "errorMessage" to match JSP source: 3)
        request.setAttribute("errorMessage", error);

        // 2. Create a temporary User object to hold the input values
        User tempUser = new User();

        // 3. Manually parse the Name again (since the form sends fullName, not firstName)
        String fullName = request.getParameter("fullName");
        String[] parts = (fullName != null) ? fullName.trim().split(" ", 2) : new String[]{"", ""};
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        // 4. Set the values user just typed into the temp object
        tempUser.setFirstName(firstName);
        tempUser.setLastName(lastName);
        tempUser.setEmail(request.getParameter("email"));
        tempUser.setPhoneNumber(request.getParameter("phoneNumber"));
        tempUser.setAddress(request.getParameter("address"));
        tempUser.setBio(request.getParameter("bio"));

        // 5. Keep the original avatar and username (since they aren't changing in the text inputs)
        HttpSession session = request.getSession();
        User realUser = (User) session.getAttribute("user");
        if (realUser != null) {
            tempUser.setAvatar(realUser.getAvatar());
            tempUser.setUsername(realUser.getUsername());
        }

        // 6. OVERRIDE the "user" attribute in the Request scope
        // This makes ${user.email} in JSP show the typed email, not the session email
        request.setAttribute("user", tempUser);

        request.getRequestDispatcher("account.jsp").forward(request, response);
    }
}