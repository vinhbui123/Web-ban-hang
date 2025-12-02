package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDao {

    // Lấy tất cả chất liệu
    public List<Material> getAll() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT id, name FROM materials";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setName(rs.getString("name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    // Lấy chất liệu theo ID
    public Material getById(int id) {
        String query = "SELECT id, name FROM materials WHERE id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Material material = new Material();
                    material.setId(rs.getInt("id"));
                    material.setName(rs.getString("name"));
                    return material;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách sản phẩm theo chất liệu
    public List<Integer> getProductIdsByMaterialId(int materialId) {
        List<Integer> productIds = new ArrayList<>();
        String query = "SELECT product_id FROM product_materials WHERE material_id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productIds.add(rs.getInt("product_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productIds;
    }
    public boolean addMaterial(String name) {
        String query = "INSERT INTO materials (name) VALUES (?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMaterial(int id, String name) {
        String query = "UPDATE materials SET name = ? WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMaterial(int id) {
        String query = "DELETE FROM materials WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}