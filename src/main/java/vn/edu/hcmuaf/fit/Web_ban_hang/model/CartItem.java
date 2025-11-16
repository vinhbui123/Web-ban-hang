package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int id;
    private int productId;
    private String productName;
    private int quantity;
    private int price;
    private int totalPrice;

    public CartItem(){}

    public CartItem(int id, int productId, String productName, int quantity, int price) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getTotalPrice() { return totalPrice; }
}
