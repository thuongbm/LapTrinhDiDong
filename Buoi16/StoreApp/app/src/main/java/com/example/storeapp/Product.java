package com.example.storeapp;

public class Product {
    private int id;
    private String name;
    private String price;
    private String details;
    private boolean isDiscount;

    public Product(int id, String name, String price, String details, boolean isDiscount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.isDiscount = isDiscount;
    }

   public String CheckDiscountStatus() {
        if (this.isDiscount) {
            return "The product is on sale";
        } else {
            return "The product is not on sale";
        }
   }

    // Getters and Setters (Used to access and update data)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public boolean isDiscount() { return isDiscount; }
    public void setDiscount(boolean discount) { isDiscount = discount; }
}
