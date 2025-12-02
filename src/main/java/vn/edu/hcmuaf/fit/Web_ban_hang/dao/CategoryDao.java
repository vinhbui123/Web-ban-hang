package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Category;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    public List<Category> getAll() {
        List<Category> category = new ArrayList<Category>();
        String query = "select * from categories";
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                category.add(new Category(id, name));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public Category getById(int id) {
        String query = "Select * from categories where id=?";
        Category category = null;
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,id);
            try(ResultSet rs=statement.executeQuery()){
                if(rs.next()){
                    category=new Category(rs.getInt(1), rs.getString(2));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();

        }
        return category;
    }
    public List<Material> getMaterialsByCategoryId(int categoryId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT DISTINCT m.id, m.name " +
                "FROM products p " +
                "JOIN product_materials pm ON p.id = pm.product_id " +
                "JOIN materials m ON pm.material_id = m.id " +
                "WHERE p.catalog_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                materials.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }

}
