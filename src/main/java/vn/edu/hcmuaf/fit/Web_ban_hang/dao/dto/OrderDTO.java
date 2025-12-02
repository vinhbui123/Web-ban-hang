package vn.edu.hcmuaf.fit.Web_ban_hang.dao.dto;

import vn.edu.hcmuaf.fit.Web_ban_hang.utils.SettingDAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private int id;
    private int userId;
    private int status;
    private int freeShipping;
    private int paymentTypeId;
    private List<DetailOrderDTO> details;

    private String statusString;
    private List<PurchaseItem> purchaseItems;
    private Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatusString(String statusDetails) {
        this.statusString = statusDetails;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString() {
        this.statusString = SettingDAO.toStatusDetails(this.status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFreeShipping() {
        return freeShipping;
    }

    public int getStatus() {
        return status;
    }

    public List<DetailOrderDTO> getDetails() {
        return details;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFreeShipping(int freeShipping) {
        this.freeShipping = freeShipping;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    // next code: tự động add nếu trùng trong details
    public boolean addDetail(DetailOrderDTO detail) {
       return this.details.add(detail);
    }
    public boolean removeDetail(DetailOrderDTO detail) {
        return this.details.remove(detail);
    }



    public OrderDTO() {}

    public OrderDTO(int uid, int status, int freeShipping, int paymentTypeId, List<DetailOrderDTO> details) {
        this.userId = uid;
        this.paymentTypeId = paymentTypeId;
        this.freeShipping = freeShipping;
        this.status = status;
        this.details = details;
        statusString = SettingDAO.toStatusDetails(status);
    }

    public OrderDTO(int uid, int status, int freeShipping, int paymentTypeId) {
        this.userId = uid;
        this.paymentTypeId = paymentTypeId;
        this.freeShipping = freeShipping;
        this.status = status;
        this.details = new ArrayList<DetailOrderDTO>();
        statusString = SettingDAO.toStatusDetails(status);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "userId=" + userId +
                ", status=" + status +
                ", freeShipping=" + freeShipping +
                ", paymentTypeId=" + paymentTypeId +
                ", details=" + details +
                '}';
    }


}
