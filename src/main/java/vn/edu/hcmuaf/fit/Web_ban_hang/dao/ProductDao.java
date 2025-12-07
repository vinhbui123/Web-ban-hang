package vn.edu.hcmuaf.fit.Web_ban_hang.dao;

import vn.edu.hcmuaf.fit.Web_ban_hang.db.DBConnect;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Color;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Material;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDao {
    static Map<Integer, Product> data = new HashMap<>();

    // Phương thức lấy tất cả các sản phẩm
    public List<Product> getAll() {
        Map<Integer, Product> productMap = new LinkedHashMap<>();

        String query = "SELECT " +
                "p.id AS product_id, p.name, p.price, p.discount, p.view, p.img, " +
                "p.catalog_id, p.description, p.created_at, p.updated_at, " +
                "COALESCE(i.quantity_in + i.quantity_returned - i.quantity_out - i.quantity_damaged, 0) AS stock, " +
                "m.id AS material_id, m.name AS material_name " +
                "FROM products p " +
                "LEFT JOIN inventory i ON p.id = i.product_id " +
                "LEFT JOIN product_materials pm ON p.id = pm.product_id " +
                "LEFT JOIN materials m ON pm.material_id = m.id " +
                "ORDER BY p.id";

        try (Connection connection = DBConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                Product p = productMap.get(productId);

                if (p == null) {
                    p = new Product();
                    p.setId(productId);
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("price"));
                    p.setDiscount(rs.getInt("discount"));
                    p.setView(rs.getInt("view"));
                    p.setImg(rs.getString("img"));
                    p.setCatalog_id(rs.getInt("catalog_id"));
                    p.setDescription(rs.getString("description"));
                    p.setStock(rs.getInt("stock"));
                    p.setMaterials(new ArrayList<>());
                    productMap.put(productId, p);
                }
                System.out.println(productId);
                int materialId = rs.getInt("material_id");
                String materialName = rs.getString("material_name");

                if (materialId > 0 && materialName != null) {
                    Material m = new Material();
                    m.setId(materialId);
                    m.setName(materialName);
                    p.getMaterials().add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(productMap.values());
    }

    // Phương thức lấy sản phẩm theo id
    public Product getById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setCatalog_id(rs.getInt("catalog_id"));
                    p.setPrice(rs.getInt("price"));
                    p.setDiscount(rs.getInt("discount"));
                    p.setView(rs.getInt("view"));
                    p.setImg(rs.getString("img"));
                    p.setDescription(rs.getString("description"));
                    p.setColors(getColorsByProductId(p.getId()));
                    p.setMaterials(getMaterialsByProductId(p.getId()));
                    p.setSubImg(getSubImagesByProductId(p.getId()));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }

    public List<Product> getByCategory(int categoryId) {
        Map<Integer, Product> productMap = new LinkedHashMap<>();

        String query = "SELECT " +
                "p.id AS product_id, p.name, p.price, p.discount, p.view, p.img, " +
                "p.catalog_id, p.description, p.created_at, p.updated_at, " +
                "COALESCE(i.quantity_in + i.quantity_returned - i.quantity_out - i.quantity_damaged, 0) AS stock, " +
                "m.id AS material_id, m.name AS material_name " +
                "FROM products p " +
                "LEFT JOIN inventory i ON p.id = i.product_id " +
                "LEFT JOIN product_materials pm ON p.id = pm.product_id " +
                "LEFT JOIN materials m ON pm.material_id = m.id " +
                "WHERE p.catalog_id = ? " +
                "ORDER BY p.id ASC";

        try (Connection connection = DBConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                Product p = productMap.get(productId);

                if (p == null) {
                    p = new Product();
                    p.setId(productId);
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("price"));
                    p.setDiscount(rs.getInt("discount"));
                    p.setView(rs.getInt("view"));
                    p.setImg(rs.getString("img"));
                    p.setCatalog_id(rs.getInt("catalog_id"));
                    p.setDescription(rs.getString("description"));
                    p.setStock(rs.getInt("stock"));
                    p.setMaterials(new ArrayList<>());
                    productMap.put(productId, p);
                }

                int materialId = rs.getInt("material_id");
                String materialName = rs.getString("material_name");

                if (materialId > 0 && materialName != null) {
                    Material m = new Material();
                    m.setId(materialId);
                    m.setName(materialName);
                    p.getMaterials().add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(productMap.values());
    }

    // Lấy màu sắc của sản phẩm
    private static List<Color> getColorsByProductId(int id) throws SQLException {
        List<Color> colors = new ArrayList<>();
        String query = "SELECT pc.color_id, c.name FROM product_color pc JOIN colors c ON c.id = pc.color_id WHERE pc.product_id = ?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Color color = new Color();
                color.setId(rs.getInt("color_id"));
                color.setName(rs.getString("name"));
                colors.add(color);
            }
        }
        return colors;
    }

    // Lấy chất liệu của sản phẩm
    private static List<Material> getMaterialsByProductId(int id) throws SQLException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT pm.material_id, m.name FROM product_materials pm JOIN materials m ON m.id = pm.material_id WHERE product_id = ?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("material_id"));
                material.setName(rs.getString("name"));
                materials.add(material);
            }
        }
        return materials;
    }

    // Lấy hình ảnh phụ của sản phẩm
    private static List<String> getSubImagesByProductId(int id) throws SQLException {
        List<String> subImages = new ArrayList<>();
        String query = "SELECT img_path FROM images WHERE product_id = ?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subImages.add(rs.getString("img_path"));
            }
        }
        return subImages;
    }

    public void increaseView(int productId) {
        String query = "UPDATE products SET view = view + 1 WHERE id=?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            Product p = getById(productId);
            System.out.println("view tăng:" + p.getView());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> re = new ArrayList();
        String query = "SELECT id, name, price, discount,view, img FROM products WHERE name LIKE ? ORDER BY RAND()";

        try {
            Connection connection = DBConnect.getConnection();

            try {
                PreparedStatement statement = connection.prepareStatement(query);

                try {
                    statement.setString(1, "%" + keyword + "%");
                    ResultSet rs = statement.executeQuery();

                    try {
                        while (rs.next()) {
                            Product p = new Product();
                            p.setId(rs.getInt("id"));
                            p.setName(rs.getString("name"));
                            p.setPrice(rs.getInt("price"));
                            p.setDiscount(rs.getInt("discount"));
                            p.setView(rs.getInt("view"));
                            p.setImg(rs.getString("img"));
                            re.add(p);
                        }
                    } catch (Throwable var12) {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                        }

                        throw var12;
                    }

                    if (rs != null) {
                        rs.close();
                    }
                } catch (Throwable var13) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var10) {
                            var13.addSuppressed(var10);
                        }
                    }

                    throw var13;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var14) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var9) {
                        var14.addSuppressed(var9);
                    }
                }

                throw var14;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var15) {
            SQLException e = var15;
            e.printStackTrace();
        }

        return re;
    }

    public List<Product> getProductViewest(int limit) {
        List<Product> re = new ArrayList<>();
        String query = "SELECT id, name, price, discount, view, img FROM products ORDER BY view DESC LIMIT ?";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setView(rs.getInt("view"));
                p.setImg(rs.getString("img"));
                re.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return re;
    }

    public List<Product> getProductsByLimit(int offset, int size) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, price, discount, view, img, quantity FROM products LIMIT ?, ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, size);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setView(rs.getInt("view"));
                p.setImg(rs.getString("img"));
                p.setQuantity(rs.getInt("quantity"));

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> getProductsInStock() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.price, p.discount, p.view, p.img, p.catalog_id, " +
                "COALESCE(i.quantity, 0) as stock " +
                "FROM products p " +
                "LEFT JOIN inventory i ON p.id = i.product_id " +
                "WHERE i.quantity > 0";

        try (Connection connection = DBConnect.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getInt("price"));
                    product.setDiscount(rs.getInt("discount"));
                    product.setView(rs.getInt("view"));
                    product.setImg(rs.getString("img"));
                    product.setCatalog_id(rs.getInt("catalog_id"));
                    product.setStock(rs.getInt("stock"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Lấy các sản phẩm có lượt xem lớn hơn một ngưỡng nhất định (ví dụ 200)
    public List<Product> getProductsViewedAbove(int minView) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, name, price, discount, view, img FROM products WHERE view >= ? ORDER BY view DESC";

        try (Connection connection = DBConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, minView);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setView(rs.getInt("view"));
                p.setImg(rs.getString("img"));
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getTopRatedProducts() {
        String query = "SELECT p.id, p.name, p.price, p.discount, p.img, p.view, " +
                "AVG(c.rating) AS avg_rating " +
                "FROM products p " +
                "JOIN comments c ON p.id = c.product_id " +
                "GROUP BY p.id, p.name, p.price, p.discount, p.img, p.view " +
                "HAVING AVG(c.rating) > 4 " +
                "ORDER BY avg_rating DESC";

        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice((int) rs.getDouble("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setImg(rs.getString("img"));
                p.setView(rs.getInt("view"));
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}