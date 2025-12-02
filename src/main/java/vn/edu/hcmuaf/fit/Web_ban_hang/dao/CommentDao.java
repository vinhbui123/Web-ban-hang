package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    // ✅ Lấy danh sách comment theo productId, kèm theo username nếu có
    public List<Comment> getCommentsByProductId(int productId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.*, u.username " +
                "FROM comments c " +
                "JOIN ( " +
                "    SELECT user_id, MAX(created_at) AS latest_time " +
                "    FROM comments " +
                "    WHERE product_id = ? " +
                "    GROUP BY user_id " +
                ") latest ON c.user_id = latest.user_id AND c.created_at = latest.latest_time " +
                "LEFT JOIN users u ON c.user_id = u.id " +
                "WHERE c.product_id = ? " +
                "ORDER BY c.created_at DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId); // cho subquery
            stmt.setInt(2, productId); // cho outer query
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment cmt = new Comment();
                cmt.setId(rs.getInt("id"));
                cmt.setProductId(rs.getInt("product_id"));
                cmt.setUserId(rs.getInt("user_id"));
                cmt.setContent(rs.getString("content"));
                cmt.setRating(rs.getInt("rating"));
                cmt.setCreatedAt(rs.getTimestamp("created_at"));

                String username = rs.getString("username");
                if (username != null && !username.isEmpty()) {
                    cmt.setUserName(username);
                } else {
                    cmt.setUserName("Ẩn danh");
                }

                comments.add(cmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    // Tính điểm đánh giá trung bình theo productId
    public double getAverageRatingByProductId(int productId) {
        String query = "SELECT AVG(rating) AS avg FROM comments WHERE product_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Thêm comment mới
    public void addComment(Comment comment) {
        String query = "INSERT INTO comments (product_id, user_id, content, rating, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, comment.getProductId());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            stmt.setInt(4, comment.getRating());
            stmt.setTimestamp(5, comment.getCreatedAt());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Lấy tất cả comment (cho admin)
    public List<Comment> getAllComments() {
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT c.*, u.username FROM comments c LEFT JOIN users u ON c.user_id = u.id ORDER BY c.created_at DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getInt("id"));
                c.setProductId(rs.getInt("product_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setRating(rs.getInt("rating"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setUserName(rs.getString("username"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Xoá comment
    public void deleteCommentById(int id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
