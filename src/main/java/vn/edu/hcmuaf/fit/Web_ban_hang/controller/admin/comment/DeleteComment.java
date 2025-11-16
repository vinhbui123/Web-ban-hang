package vn.edu.hcmuaf.fit.Web_ban_hang.controller.admin.comment;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CommentDao;

import java.io.IOException;

@WebServlet(urlPatterns = "/adminDelete")
public class DeleteComment extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        CommentDao dao = new CommentDao();
        dao.deleteCommentById(id);
        resp.sendRedirect("adminComment");
    }
}

