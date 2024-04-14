package com.om_tat_sat.goodsguardian.model;

public class Items_holder {
    private int id;
    private String name;
    private String description;
    private Integer quantity;
    private String category;
    private String expiry_date;
    private String image;

    public Items_holder(int id, String name, String description, Integer quantity, String category, String expiry_date, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
        this.expiry_date = expiry_date;
        this.image = image;
    }
    public Items_holder(String name, String description, Integer quantity, String category, String expiry_date, String image) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.category = category;
        this.expiry_date = expiry_date;
        this.image = image;
    }
    public Items_holder(){

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
