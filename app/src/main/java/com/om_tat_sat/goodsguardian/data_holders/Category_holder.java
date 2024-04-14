package com.om_tat_sat.goodsguardian.data_holders;

public class Category_holder {
    String name;
    String quantity;

    public Category_holder(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = String.valueOf(quantity);
    }
}
