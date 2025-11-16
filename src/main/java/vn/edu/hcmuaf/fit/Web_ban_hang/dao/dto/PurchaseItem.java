package vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto;

import vn.edu.hcmuaf.fit.Web_ban_hang.utils.SettingDAO;

public class PurchaseItem {

    private int idUser;
    private int idDetail;
    private String img;
    private String name;
    private int quantity;
    private int discount;
    private int total;
    private int status;
    private String statusString;
    private int idProduct;

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
    public String getStatusString() {
        return statusString;
    }
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public int getTotal() {
        return total;
    }

    public int getStatus() {
        return status;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public PurchaseItem() {}

    public PurchaseItem(String img, String name, int discount, int quantity, int total, int status) {
        this.img = img;
        this.name = name;
        this.discount = discount;
        this.quantity = quantity;
        this.total = total;
        this.status = status;
        statusString = SettingDAO.toStatusDetails(status);
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "idUser=" + idUser +
                ", idDetail=" + idDetail +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", discount=" + discount +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
