package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;
    private int status;
    private int userId;
    private int freeShipping;
    private int paymentTypeId;
    private int totalAmount;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public Order() {}

    // Constuctor to get info from web to DB
    public Order(int status, int userId, int freeShipping, int paymentTypeId) {
        this.status = status;
        this.userId = userId;
        this.freeShipping = freeShipping;
        this.paymentTypeId = paymentTypeId;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFreeShipping() { return freeShipping; }
    public void setFreeShipping(int freeShipping) { this.freeShipping = freeShipping; }

    public int getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(int paymentTypeId) { this.paymentTypeId = paymentTypeId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", userId=" + userId +
                ", freeShipping=" + freeShipping +
                ", paymentTypeId=" + paymentTypeId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

}

