package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.comment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CommentDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Comment;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/adminComment")
public class AdminComment extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        CommentDao dao = new CommentDao();
        List<Comment> comments = dao.getAllComments(); // bạn sẽ viết hàm này
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/ad-comment.jsp").forward(req, resp);
    }
}

