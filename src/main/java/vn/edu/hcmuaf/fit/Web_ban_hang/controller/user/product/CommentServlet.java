package vn.edu.hcmuaf.fit.Web_ban_hang.controller.user.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.Web_ban_hang.dao.CommentDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Comment;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "CommentServlet", value = "/comment")
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getId();
        int productId = Integer.parseInt(req.getParameter("productId"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String content = req.getParameter("content");

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setProductId(productId);
        comment.setRating(rating);
        comment.setContent(content);
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        CommentDao commentDao = new CommentDao();
        commentDao.addComment(comment);

        resp.sendRedirect("product-detail?id=" + productId);
    }
}
