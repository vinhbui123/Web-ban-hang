package vn.edu.hcmuaf.fit.Web_ban_hang.dao.session;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.InventoryDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;
import vn.edu.hcmuaf.fit.Web_ban_hang.services.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Cart {
    Map<Integer, CartProduct> data = new HashMap<>();


    public boolean add(Product p, int quantity) {
        if (data.containsKey(p.getId())) {
            update(p.getId(), data.get(p.getId()).getQuantity() + quantity);
        } else {
            // Nếu sản phẩm chưa tồn tại, thêm mới với số lượng ban đầu
            CartProduct cartProduct = convert(p);
            cartProduct.setQuantity(quantity);
            data.put(p.getId(), cartProduct);
        }
        return true;
    }

    public boolean addByID(Integer id, int quantity){
        if (data.containsKey(id)) {
            return update(id, data.get(id).getQuantity() + quantity);
        } else {
            // Nếu sản phẩm chưa tồn tại, thêm mới với số lượng ban đầu
            ProductService productService = new ProductService();
            InventoryDao inventoryDao = new InventoryDao();
            int stock = inventoryDao.getStock(id);
            if (quantity > stock) {
                return false;
            }
            CartProduct cartProduct = convert(productService.getById(id));
            cartProduct.setQuantity(quantity);
            data.put(id, cartProduct);
        }
        return true;
    }

    public boolean update(int id, int quantity) {
        if (!data.containsKey(id)) return false;
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
        return true;
    }

    public boolean remove(int id) {
        if (!data.containsKey(id)) return false;
        return data.remove(id) != null;
    }

    public boolean removeAll() {
        if (data.isEmpty()) return false;
        data.clear();
        return true;
    }


    public List<CartProduct> getList() {
        return new ArrayList<>(data.values());
    }

    public int getTotalQuantityAll() {
        AtomicInteger total = new AtomicInteger(0);
        data.values().stream().forEach(cartProduct -> total.addAndGet(cartProduct.getQuantity()));
        return total.get();
    }

    public Double getTotal() {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        data.values().stream().forEach(cartProduct -> total.updateAndGet(v -> v + (cartProduct.getQuantity() * cartProduct.getPrice())));
        return total.get();
    }

    public int getTotalProducts(boolean uniqueOnly) {
        return uniqueOnly ? data.size() : data.values().stream().mapToInt(CartProduct::getQuantity).sum();
    }

    private CartProduct convert(Product p) {
        CartProduct re = new CartProduct();
        re.setId(p.getId());
        re.setName(p.getName());
        re.setPrice(p.getPrice());
        re.setImg(p.getImg());
        re.setQuantity(1);
        return re;
    }

    public boolean setStock() {
        if (data.isEmpty()) return false;
        InventoryDao inventoryDao = new InventoryDao();
        for (CartProduct cp : data.values()) {
            cp.setStock(inventoryDao.getStock(cp.getId()));
        }
        return true;
    }
}
