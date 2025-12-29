package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    private int id;
    private int catalog_id;
    private String name;
    private String img;
    private int price;
    private int discount;
    private int view;
    private String description;
    private Date created_at;
    private Date updated_at;
    private List<String> subImg;
    private List<Color> colors;
    private List<Material> materials;
    private int quantity;
    private int stock;

    // Default constructor
    public Product() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalogId) {
        this.catalog_id = catalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public List<String> getSubImg() {
        return subImg;
    }

    public void setSubImg(List<String> subImg) {
        this.subImg = subImg;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", catalog_id=" + catalog_id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", view=" + view +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", subImg=" + subImg +
                ", colors=" + colors +
                ", materials=" + materials +
                '}';
    }
}
