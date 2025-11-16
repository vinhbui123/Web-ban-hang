package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private int id;
    private int orderId;
    private int productId;
    private int price;
    private int quantity;
    private int totalMoney;
    private int discountPercentage;
    private int discountAmount;
    private int status;
    private String dateAllocated;

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }

    public int getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(int discountAmount) { this.discountAmount = discountAmount; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getDateAllocated() { return dateAllocated; }
    public void setDateAllocated(String dateAllocated) { this.dateAllocated = dateAllocated; }

    // Constructor
    public OrderDetail() {}

    public OrderDetail(int productId, int price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public int getTotalMoney() { return this.price * this.quantity; }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    // Constuctor to get info from web to DB
    public OrderDetail(int productId, int price, int quantity,
                       int discountPercentage, int discountAmount, int status) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.status = status;

        this.totalMoney = price * quantity;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalMoney=" + totalMoney +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", status=" + status +
                ", dateAllocated='" + dateAllocated + '\'' +
                '}';
    }
}
