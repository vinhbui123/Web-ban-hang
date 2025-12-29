package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.comments;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CommentDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Comment;

import java.io.IOException;
import java.util.List;

// URL Pattern chung cho trang quản lý comment
@WebServlet(urlPatterns = "/adminComments")
public class AdminCommentController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminCommentController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // hiển thị danh sách
        if (action == null || action.equals("list")) {
            CommentDao dao = new CommentDao();
            List<Comment> comments = dao.getAllComments();

            req.setAttribute("comments", comments);
            req.getRequestDispatcher("/ad-comment.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Kiểm tra nếu action là delete thì thực hiện xóa
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                CommentDao dao = new CommentDao();
                dao.deleteCommentById(id);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
            }
            // Xóa xong thì redirect về lại trang danh sách (gọi lại doGet)
            resp.sendRedirect(req.getContextPath() + "/adminComments");
        }
        // Nếu không phải delete (hoặc logic khác) thì cũng về trang chủ admin-comment
        else {
            resp.sendRedirect(req.getContextPath() + "/adminComments");
        }
    }
}