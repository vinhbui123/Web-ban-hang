package vn.edu.hcmuaf.fit.Web_ban_hang.session;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.CartProduct;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quản lý giỏ hàng trong session
 * Lưu trữ Map các CartProduct theo productId
 */
public class Cart implements Serializable {
    private Map<Integer, CartProduct> data = new HashMap<>();

    /**
     * Thêm sản phẩm vào giỏ từ Product object
     */
    public boolean add(Product p, int quantity) {
        if (data.containsKey(p.getId())) {
            // Nếu sản phẩm đã tồn tại, cập nhật số lượng
            return update(p.getId(), data.get(p.getId()).getQuantity() + quantity);
        } else {
            // Nếu sản phẩm chưa tồn tại, thêm mới
            InventoryDao inventoryDao = new InventoryDao();
            int stock = inventoryDao.getStock(p.getId());
            if (quantity > stock) {
                return false; // Không đủ hàng trong kho
            }
            CartProduct cartProduct = convert(p);
            cartProduct.setQuantity(quantity);
            cartProduct.setStock(stock);
            data.put(p.getId(), cartProduct);
        }
        return true;
    }

    /**
     * Thêm sản phẩm vào giỏ bằng productId
     */
    public boolean addById(int id, int quantity) {
        if (data.containsKey(id)) {
            return update(id, data.get(id).getQuantity() + quantity);
        } else {
            ProductService productService = new ProductService();
            InventoryDao inventoryDao = new InventoryDao();
            int stock = inventoryDao.getStock(id);
            if (quantity > stock) {
                return false;
            }
            Product product = productService.getById(id);
            if (product == null) {
                return false;
            }
            CartProduct cartProduct = convert(product);
            cartProduct.setQuantity(quantity);
            cartProduct.setStock(stock);
            data.put(id, cartProduct);
        }
        return true;
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    public boolean update(int id, int quantity) {
        if (!data.containsKey(id))
            return false;

        InventoryDao inventoryDao = new InventoryDao();
        int stock = inventoryDao.getStock(id);

        if (quantity <= 0) {
            remove(id);
            return true;
        }
        if (quantity > stock) {
            return false; // Không đủ hàng
        }

        CartProduct cp = data.get(id);
        cp.setQuantity(quantity);
        cp.setStock(stock);
        return true;
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public boolean remove(int id) {
        if (!data.containsKey(id))
            return false;
        return data.remove(id) != null;
    }

    /**
     * Xóa tất cả sản phẩm trong giỏ
     */
    public boolean removeAll() {
        if (data.isEmpty())
            return false;
        data.clear();
        return true;
    }

    /**
     * Lấy danh sách các sản phẩm trong giỏ
     */
    public List<CartProduct> getList() {
        return new ArrayList<>(data.values());
    }

    /**
     * Tổng số lượng tất cả sản phẩm
     */
    public int getTotalQuantityAll() {
        return data.values().stream()
                .mapToInt(CartProduct::getQuantity)
                .sum();
    }

    /**
     * Tổng tiền (không tính giảm giá)
     */
    public double getTotal() {
        return data.values().stream()
                .mapToDouble(cp -> cp.getPrice() * cp.getQuantity())
                .sum();
    }

    /**
     * Tổng tiền (có tính giảm giá)
     */
    public double getTotalWithDiscount() {
        return data.values().stream()
                .mapToDouble(cp -> cp.getDiscountedPrice() * cp.getQuantity())
                .sum();
    }

    /**
     * Số loại sản phẩm (unique) hoặc tổng số lượng
     */
    public int getTotalProducts(boolean uniqueOnly) {
        return uniqueOnly ? data.size() : getTotalQuantityAll();
    }

    /**
     * Kiểm tra giỏ hàng có trống không
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Cập nhật stock cho tất cả sản phẩm trong giỏ
     */
    public boolean refreshStock() {
        if (data.isEmpty())
            return false;
        InventoryDao inventoryDao = new InventoryDao();
        for (CartProduct cp : data.values()) {
            cp.setStock(inventoryDao.getStock(cp.getId()));
        }
        return true;
    }

    /**
     * Chuyển đổi Product sang CartProduct
     */
    private CartProduct convert(Product p) {
        CartProduct cp = new CartProduct();
        cp.setId(p.getId());
        cp.setName(p.getName());
        cp.setPrice(p.getPrice());
        cp.setDiscount(p.getDiscount());
        cp.setImg(p.getImg());
        cp.setQuantity(1);
        return cp;
    }
}
