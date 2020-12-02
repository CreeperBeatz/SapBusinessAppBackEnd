package com.company.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Datasource {

    //Database connection
    public static final String DB_NAME = "project.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\" + DB_NAME;

    //Database constants
    public static final String TABLE_USERS = "users";
        public static final String COLUMN_USERS_ID = "=_id";
        public static final String COLUMN_USERS_USERNAME = "username";
        public static final String COLUMN_USERS_EMAIL = "email";
        public static final String COLUMN_USERS_PASSWORD_HASH = "hash";
        public static final String COLUMN_USERS_TYPE = "type";

    public static final String TABLE_SALES = "sales";
        public static final String COLUMN_SALES_ID = "_id";
        public static final String COLUMN_SALES_SALESMAN = "salesman";
        public static final String COLUMN_SALES_PRODUCT = "product";
        public static final String COLUMN_SALES_QUANTITY = "quantity";
        public static final String COLUMN_SALES_DISCOUNT = "discount";
        public static final String COLUMN_SALES_PRICE = "price";

    public static final String TABLE_PRODUCTS = "products";
        public static final String COLUMN_PRODUCTS_ID = "_id";
        public static final String COLUMN_PRODUCTS_NAME = "name";
        public static final String COLUMN_PRODUCTS_PRICE = "price";
        public static final String COLUMN_PRODUCTS_DISCOUNT = "discount";
        public static final String COLUMN_PRODUCTS_DESCRIPTION = "description";
        public static final String COLUMN_PRODUCTS_IMAGE_URL = "imageUrl";

    public static final String TABLE_CLIENTS = "clients";
        public static final String COLUMN_CLIENTS_ID = "_id";
        public static final String COLUMN_CLIENTS_NAME = "name";
        public static final String COLUMN_CLIENTS_SURNAME = "surname";
        public static final String COLUMN_CLIENTS_ADDRESS = "address";
        public static final String COLUMN_CLIENTS_COUNTRY = "country";
        public static final String COLUMN_CLIENTS_CITY = "city";
        public static final String COLUMN_CLIENTS_POSTAL_CODE = "postalCode";
        public static final String COLUMN_CLIENTS_NUMBER_OF_PURCHASES = "purchases";


    private Connection conn;

    public boolean open(){
        try {
           conn = DriverManager.getConnection(CONNECTION_STRING);
           return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            if(conn!=null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////////////////ADD DATA///////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public void insertUser(String username, String password_hash, String email, byte userType){
        //TODO validation
        //TODO write to sql database
    }

    public void insertClient(){};
    public void insertProduct(){};
    public void insertSale(){};

    ///////////////////////////////////////////////////////////////////
    ///////////////////////////REMOVE DATA/////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public void removeUser(){};
    public void removeClient(){};
    public void removeProduct(){};
    public void removeSale(){}; //questionable, but yeah

    ///////////////////////////////////////////////////////////////////
    //////////////////////////CHANGE DATA//////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public void changeUser(){};
    public void changeClient(){};
    public void changeProduct(){};
    public void changeSale(){};

    ///////////////////////////////////////////////////////////////////
    //////////////////////////////QUERIES//////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public ResultSet getSalesBySalesMan(String salesman){return null;}
    public ResultSet getSalesByDate(long fromMillisec, long toMillisec){return null;}
    public ResultSet getUsers(){return null;}
    public ResultSet getProductsByPrice(float fromPrice, float toPrice){return null;}
    public ResultSet getProductsAll(){return null;}

    ///////////////////////////////////////////////////////////////////
    ///////////////////////STATIC METHODS//////////////////////////////
    ///////////////////////////////////////////////////////////////////

    public static void createDataBase(){};
    //TODO static void that creates database with 1 record (admin), if there's no database
}
