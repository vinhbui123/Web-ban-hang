package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.io.Serializable;

public class CartProduct implements Serializable {
    private int id;
    private String name;
    private int quantity;
    private String img;
    private int price;
    private int discount;
    private boolean selected;
    private int stock;

    public CartProduct() {
        this.selected = true; // Default to selected when added to cart
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}