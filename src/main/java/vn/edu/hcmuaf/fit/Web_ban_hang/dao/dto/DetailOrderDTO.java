package vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto;

public class DetailOrderDTO {
    private int productId;
    private int price;
    private int quantity;

    private int discountPercentage;
    private int discountAmount;

    public int getProductId() {
        return productId;
    }


    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public DetailOrderDTO() {
    }

    public DetailOrderDTO(int productId, int price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "DetailOrderDTO{" +
                "productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
