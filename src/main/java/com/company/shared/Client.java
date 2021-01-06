package com.company.shared;

public class Client {

    private int id;
    private String name;
    private String surname;
    private String address;
    private String country;
    private String city;
    private int postalCode;
    private int numOfPurchases;

    public Client() {
    }

    public Client(int id , String name , String surname , String address , String country , String city , int postalCode , int numOfPurchases) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.numOfPurchases = numOfPurchases;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getNumOfPurchases() {
        return numOfPurchases;
    }

    public void setNumOfPurchases(int numOfPurchases) {
        this.numOfPurchases = numOfPurchases;
    }

    @Override
    public String toString(){
        return this.getName() + " " + this.getSurname() + " | " + this.getAddress();
    }
}
