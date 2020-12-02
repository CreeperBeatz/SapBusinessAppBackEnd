package com.company;

import java.sql.ResultSet;

public class Database {

    //Database connection
    public static final String DB_NAME = "project.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\" + DB_NAME;

    //Database creation
    public static final String TABLE_USERS = "users";
        public static final String COLUMN_USERS_ID = "=_id";
        public static final String COLUMN_USERS_USERNAME = "username";
        public static final String COLUMN_USERS_EMAIL = "email";
        public static final String COLUMN_USERS_TYPE = "type";
    public static final String TABLE_SALES = "sales";
        public static final String COLUMN_SALES_ID = "_id";
        public static final String COLUMN_SALES_SALESMAN = "salesman";
        public static final String COLUMN_SALES_PRODUCT = "product";
        public static final String COLUMN_SALES_QUANTITY = "quantity";
        public static final String COLUMN_SALES_PROMOTION = "promotion";
        public static final String COLUMN_SALES_PRICE = "price";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_CLIENTS = "clients";

    public Database() {
        //TODO open database, if not exists, throw exception, catch it in main, ask user if they want to create one

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
