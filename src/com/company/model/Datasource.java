package com.company.model;

import com.company.utilities.MD5Hash;

import java.io.File;
import java.sql.*;
import java.util.List;

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
        public static final String COLUMN_SALES_CLIENT = "client";
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

    //Database creation
    //TODO change all the fields (oh god) with the psfs above
    public static final String DATABASE_CREATION_STRING =
            "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"username\"      TEXT,\n" +
            "        \"email\"      TEXT,\n" +
            "        \"hash\"  TEXT,\n" +
            "        \"type\"  INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"sales\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"salesman\"      INTEGER,\n" +
            "        \"client\"        INTEGER,\n" +
            "        \"product\"       INTEGER,\n" +
            "        \"quantity\"      INTEGER,\n" +
            "        \"discount\"      INTEGER,\n" +
            "        \"price\" INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"products\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"name\"  TEXT,\n" +
            "        \"price\" NUMERIC,\n" +
            "        \"discount\"      NUMERIC,\n" +
            "        \"description\"   TEXT,\n" +
            "        \"imageUrl\"      TEXT,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"clients\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"name\"  TEXT,\n" +
            "        \"surname\"       TEXT,\n" +
            "        \"address\"       TEXT,\n" +
            "        \"country\"       TEXT,\n" +
            "        \"city\"  TEXT,\n" +
            "        \"postalCode\"    TEXT,\n" +
            "        \"purchases\"     INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "INSERT INTO users(username, email ,hash, type) VALUES('administrator', 'random@mail.com', '" +
            MD5Hash.getHash("administrator") +
            "', 1);";

    private Connection conn;

    public boolean open(){

        boolean dbExists = DBExists();

        try {
           conn = DriverManager.getConnection(CONNECTION_STRING); //creates a .db file if not exists
           if(!dbExists) {
               createDB();
           }
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

    public boolean DBExists() {
        File test = new File(DB_NAME);
        return test.exists();
    }

    private void createDB() throws SQLException{
        Statement statement = conn.createStatement();
        statement.execute(DATABASE_CREATION_STRING);
        statement.close();
        System.out.println("Default database with admin account administrator:administrator created");
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
    //TODO might have to migrate those to the client side?
    public List<Sale> getSales(){return null;}
    public List<Sale> getSales(long fromMillisec, long toMillisec, String salesman){return null;}

    public List<User> getUsers(){return null;}
    public List<User> getUsers(int type){return null;}
    public ResultSet getProductsByPrice(float fromPrice, float toPrice){return null;}
    public ResultSet getProductsAll(){return null;}

    ///////////////////////////////////////////////////////////////////
    ///////////////////////STATIC METHODS//////////////////////////////
    ///////////////////////////////////////////////////////////////////


}
