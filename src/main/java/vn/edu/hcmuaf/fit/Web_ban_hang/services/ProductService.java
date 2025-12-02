package vn.edu.hcmuaf.fit.Web_ban_hang.services;

import vn.edu.hcmuaf.fit.Web_ban_hang.dao.ProductDao;
import vn.edu.hcmuaf.fit.Web_ban_hang.model.Product;

import java.util.Collections;
import java.util.List;

public class ProductService {

    static ProductDao productDao = new ProductDao();

    public List<Product> getAll() {
        return productDao.getAll();
    }

    // Lấy sản phẩm theo ID
    public Product getById(int id) {
        return ProductDao.getById(id);
    }

    // Lấy sản phẩm theo danh mục
    public List<Product> getByCategory(int categoryId) {
        List<Product> products = productDao.getByCategory(categoryId);
        Collections.shuffle(products);
        return products;
    }

    public List<Product> getRelatetByCategory(int categoryId, int productId, int limit) {
        List<Product> products = productDao.getByCategory(categoryId);

        products.removeIf(product -> product.getId() == productId);

        if (limit > 0 && products.size() > limit) {
            products.sort((product1, product2) -> Integer.compare(product2.getView(), product1.getView()));
            products = products.subList(0, limit);
        }
        Collections.shuffle(products);

        return products;
    }

    public List<Product> searchProducts(String keyword) {
        return productDao.searchProducts(keyword);
    }

    public void increaseView(int id) {
        productDao.increaseView(id);
    }

    public List<Product> getProductViewest(int limit) {
        return productDao.getProductViewest(limit);
    }

    public List<Product> getProductsInStock() {
        return productDao.getProductsInStock();
    }
    public List<Product> getProductsViewedAbove(int minView) {
        return productDao.getProductsViewedAbove(minView);
    }
    public List<Product> getTopRatedProducts() {
        return productDao.getTopRatedProducts();
    }

}