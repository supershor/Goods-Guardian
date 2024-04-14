package com.om_tat_sat.goodsguardian.model;

public class Category_holder {
    private String name;
    private Integer quantity;
    public Category_holder(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public Category_holder() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
