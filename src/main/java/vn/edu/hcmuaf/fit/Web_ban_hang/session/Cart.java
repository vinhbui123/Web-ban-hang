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
 */
public class Cart implements Serializable {
    private Map<Integer, CartProduct> data = new HashMap<>();

    public boolean add(Product p, int quantity) {
        if (data.containsKey(p.getId())) {
            return update(p.getId(), data.get(p.getId()).getQuantity() + quantity);
        } else {
            InventoryDao inventoryDao = new InventoryDao();
            int stock = inventoryDao.getStock(p.getId());
            if (quantity > stock) {
                return false;
            }
            CartProduct cartProduct = convert(p);
            cartProduct.setQuantity(quantity);
            cartProduct.setStock(stock);
            data.put(p.getId(), cartProduct);
        }
        return true;
    }

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
            return false;
        }

        CartProduct cp = data.get(id);
        cp.setQuantity(quantity);
        cp.setStock(stock);
        return true;
    }

    public boolean remove(int id) {
        return data.remove(id) != null;
    }

    public boolean removeAll() {
        data.clear();
        return true;
    }

    public List<CartProduct> getList() {
        return new ArrayList<>(data.values());
    }

    public int getTotalQuantityAll() {
        return data.values().stream().mapToInt(CartProduct::getQuantity).sum();
    }

    public double getTotal() {
        return data.values().stream().mapToDouble(cp -> cp.getPrice() * cp.getQuantity()).sum();
    }

    public double getTotalWithDiscount() {
        return data.values().stream().mapToDouble(cp -> cp.getDiscountedPrice() * cp.getQuantity()).sum();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void refreshStock() {
        if (data.isEmpty())
            return;
        InventoryDao inventoryDao = new InventoryDao();
        for (CartProduct cp : data.values()) {
            cp.setStock(inventoryDao.getStock(cp.getId()));
        }
    }

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
