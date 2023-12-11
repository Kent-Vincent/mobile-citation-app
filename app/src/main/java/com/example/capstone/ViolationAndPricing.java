package com.example.capstone;

public class ViolationAndPricing {

    private int SortOrder;
    private String Name;
    private String Price;
    private String IconForViolationUrl;

    public ViolationAndPricing() {

    }

    public ViolationAndPricing(String name, String price, String iconForViolationUrl, int sortOrder){
        this.Name = name;
        this.Price = price;
        this.IconForViolationUrl = iconForViolationUrl;
        this.SortOrder = sortOrder;

    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getIconForViolationUrl() {
        return IconForViolationUrl;
    }

    public void setIconForViolationUrl(String iconForViolationUrl) {
        this.IconForViolationUrl = iconForViolationUrl;
    }
    public int getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.SortOrder = sortOrder;
    }
}
