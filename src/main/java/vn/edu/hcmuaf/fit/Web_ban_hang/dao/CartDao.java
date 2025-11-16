package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    // Phương thức lấy tất cả các sản phẩm trong cart theo uid
    public List<CartItem> getAllCart(int uid) {
        List<CartItem> re = new ArrayList<>();
        String query = "SELECT c.id, c.product_id, c.quantity, p.name, p.price " +
                "FROM cart c " +
                "JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";
        try (Connection connection = DBConnect.getConnection();  // Kết nối từ DBConnect
             PreparedStatement statement = connection.prepareStatement(query)) {  // Tạo PreparedStatement
            statement.setInt(1, uid);  // Gán tham số vào PreparedStatement

            try (ResultSet rs = statement.executeQuery()) {  // Thực thi truy vấn
                while (rs.next()) {
                    CartItem cart = new CartItem();
                    cart.setId(rs.getInt("id"));
                    cart.setProductId(rs.getInt("product_id"));
                    cart.setProductName(rs.getString("name"));
                    cart.setQuantity(rs.getInt("quantity"));
                    cart.setPrice(rs.getInt("price"));
                    re.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return re;
    }
    //Phương thức xóa tất cả các cartItem được chọn
    public void removeMulCart(int uid, int[] product_id) {}
    //Phương thức xóa tất cả các cartItem được chọn
    public void delCart(int product_id, int uid) {}
    //Phương thức thay đổi quantity
    public void changeQuantity(int product_id, int uid, int quantity) {}
//   public void addCart(CartItem cart) {}



}
