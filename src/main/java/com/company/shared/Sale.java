package com.company.shared;

public class Sale {
    private int id;
    private int salesManID;
    private int clientID;
    private int productID;
    private int quantity;
    private float discount;
    private float price;

    public Sale() {
    }

    public Sale(int id , int salesManID , int clientID , int productID , int quantity , float discount , float price) {
        this.id = id;
        this.salesManID = salesManID;
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
        this.discount = discount;
        this.price = price;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalesManID() {
        return salesManID;
    }

    public void setSalesManID(int salesManID) {
        this.salesManID = salesManID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
