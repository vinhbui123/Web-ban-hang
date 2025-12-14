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
        this.selected = true; // Mặc định được chọn khi thêm vào giỏ
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

    public void setName(String name) {
        this.name = name;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Tính giá sau khi giảm giá
     */
    public int getDiscountedPrice() {
        if (discount > 0) {
            return price - (price * discount / 100);
        }
        return price;
    }
}
