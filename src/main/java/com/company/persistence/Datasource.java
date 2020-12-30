package com.company.persistence;

import java.io.File;
import java.sql.*;

public class Datasource {

    //Database connection
    public static final String DB_NAME = "project.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\" + DB_NAME;

    private Connection conn;

    //Users statements
    private PreparedStatement insertUserPrep;
    private PreparedStatement deleteUserPrep;
    private PreparedStatement changeUserPrep;
    private PreparedStatement queryUserByUsername;
    private PreparedStatement queryAllUsers;
    private PreparedStatement queryAllTraders;
    private PreparedStatement queryUserByUsernameType;

    //Sales statements
    private PreparedStatement querySaleBySalesman;
    private PreparedStatement querySaleByDate;
    private PreparedStatement insertSale;
    private PreparedStatement deleteSale;

    //Products statements
    private PreparedStatement queryProductByID;
    private PreparedStatement queryProductByName;
    private PreparedStatement changeProduct;
    private PreparedStatement queryProductByPrice;
    private PreparedStatement deleteProduct;
    private PreparedStatement insertProduct;

    //Client statements
    private PreparedStatement queryClientByID;
    private PreparedStatement queryClientByName;
    private PreparedStatement queryAllClients;
    private PreparedStatement insertClient;
    private PreparedStatement deleteClient;
    private PreparedStatement changeClient;
    private PreparedStatement updateClientPurchases;

    //Singleton design pattern, Thread safe
    private static Datasource instance = new Datasource();

    private Datasource(){}

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open(){

        boolean dbExists = DBExists();

        try {
           conn = DriverManager.getConnection(CONNECTION_STRING); //creates a .db file if not exists
           if(!dbExists) {
               createDB();
           }

           //Table users
           insertUserPrep = conn.prepareStatement(TableUsers.INSERT_NEW_USER_PREP);
           deleteUserPrep = conn.prepareStatement(TableUsers.DELETE_USER_PREP);
           changeUserPrep = conn.prepareStatement(TableUsers.CHANGE_USER_PREP);
           queryUserByUsername = conn.prepareStatement(TableUsers.QUERY_USER_BY_USERNAME_PREP);
           queryAllUsers = conn.prepareStatement(TableUsers.QUERY_ALL_USERS_PREP);
           queryAllTraders = conn.prepareStatement(TableUsers.QUERY_ALL_TRADERS_PREP);
           queryUserByUsernameType = conn.prepareStatement(TableUsers.QUERY_USER_BY_USERNAME_TYPE_PREP);

           //table sales
           querySaleBySalesman = conn.prepareStatement(TableSales.QUERY_SALE_BY_TRADER_PREP);
           querySaleByDate = conn.prepareStatement(TableSales.QUERY_SALE_BY_DATE_PREP);
           insertSale = conn.prepareStatement(TableSales.INSERT_SALE_PREP);
           deleteSale = conn.prepareStatement(TableSales.DELETE_SALE_PREP);

           //table products
           queryProductByID = conn.prepareStatement(TableProducts.QUERY_PRODUCT_BY_ID_PREP);
           queryProductByName = conn.prepareStatement(TableProducts.QUERY_PRODUCT_BY_NAME_PREP);
           changeProduct = conn.prepareStatement(TableProducts.CHANGE_PRODUCT_PREP);
           queryProductByPrice = conn.prepareStatement(TableProducts.QUERY_PRODUCT_BY_PRICE_PREP);
           deleteProduct = conn.prepareStatement(TableProducts.DELETE_PRODUCT_PREP);
           insertProduct = conn.prepareStatement(TableProducts.INSERT_PRODUCT_PREP);

           //table clients
           queryClientByID = conn.prepareStatement(TableClients.QUERY_CLIENT_BY_ID_PREP);
           queryClientByName = conn.prepareStatement(TableClients.QUERY_CLIENT_BY_NAME);
           queryAllClients = conn.prepareStatement(TableClients.QUERY_ALL_CLIENTS_PREP);
           insertClient = conn.prepareStatement(TableClients.INSERT_CLIENT_PREP);
           deleteClient = conn.prepareStatement(TableClients.DELETE_CLIENT_PREP);
           changeClient = conn.prepareStatement(TableClients.CHANGE_CLIENT_PREP);
           updateClientPurchases = conn.prepareStatement(TableClients.UPDATE_CLIENT_NUM_PURCHASES_PREP);

           return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            if (conn != null) {

                //Table users
                if (insertUserPrep != null) insertUserPrep.close();
                if (deleteUserPrep != null) deleteUserPrep.close();
                if (changeUserPrep != null) changeUserPrep.close();
                if (queryUserByUsername != null) queryUserByUsername.close();
                if (queryAllUsers != null) queryAllUsers.close();
                if (queryAllTraders != null) queryAllTraders.close();
                if (queryUserByUsernameType != null) queryUserByUsernameType.close();

                //Table sales
                if (querySaleBySalesman != null) querySaleBySalesman.close();
                if (querySaleByDate != null) querySaleByDate.close();
                if (insertSale != null) insertSale.close();
                if (deleteSale != null) deleteSale.close();

                //Table products
                if (queryProductByID != null) queryProductByID.close();
                if (queryProductByName != null) queryProductByName.close();
                if (queryProductByPrice != null) queryProductByPrice.close();
                if (changeProduct != null) changeProduct.close();
                if (deleteProduct != null) deleteProduct.close();
                if (insertProduct != null) insertProduct.close();

                //Table clients
                if (queryClientByID != null) queryClientByID.close();
                if (queryAllClients != null) queryAllClients.close();
                if (queryClientByName != null) queryClientByName.close();
                if (insertClient != null) insertClient.close();
                if (deleteClient != null) deleteClient.close();
                if (changeClient != null) changeClient.close();
                if (updateClientPurchases != null) updateClientPurchases.close();

                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getQueryUserByUsernameType() {
        return  queryUserByUsernameType;
    }
    public PreparedStatement getQueryUserByUsername() {
        return queryUserByUsername;
    }
    public PreparedStatement getChangeUserPrep() {
        return changeUserPrep;
    }
    public PreparedStatement getDeleteUserPrep() {
        return deleteUserPrep;
    }
    public PreparedStatement getInsertUserPrep() {
        return insertUserPrep;
    }
    public PreparedStatement getQueryAllUsers() {
        return queryAllUsers;
    }
    public PreparedStatement getQueryAllTraders() {
        return queryAllTraders;
    }

    public PreparedStatement getQuerySaleBySalesman() {
        return querySaleBySalesman;
    }
    public PreparedStatement getQuerySaleByDate() {
        return querySaleByDate;
    }
    public PreparedStatement getInsertSale() {
        return insertSale;
    }
    public PreparedStatement getDeleteSale() {
        return deleteSale;
    }

    public PreparedStatement getQueryProductByID() {
        return queryProductByID;
    }
    public PreparedStatement getQueryProductByName() {
        return queryProductByName;
    }
    public PreparedStatement getChangeProduct() {
        return  changeProduct;
    }
    public PreparedStatement getQueryProductByPrice() {
        return queryProductByPrice;
    }
    public PreparedStatement getDeleteProduct() {
        return deleteProduct;
    }
    public PreparedStatement getInsertProduct() {
        return insertProduct;
    }

    public PreparedStatement getQueryClientByID() {
        return queryClientByID;
    }
    public PreparedStatement getQueryClientByName() {
        return queryClientByName;
    }
    public PreparedStatement getChangeClient() {
        return changeClient;
    }
    public PreparedStatement getDeleteClient() {
        return deleteClient;
    }
    public PreparedStatement getInsertClient() {
        return insertClient;
    }
    public PreparedStatement getQueryAllClients() {
        return queryAllClients;
    }
    public PreparedStatement getUpdateClientPurchases() {
        return updateClientPurchases;
    }

    public Connection getConn() {
        return conn;
    }

    private boolean DBExists() {
        File test = new File(DB_NAME);
        return test.exists();
    }

    private void createDB() throws SQLException{
        Statement statement = conn.createStatement();
        statement.execute(DBCreation.DATABASE_CREATION_STRING);
        statement.close();
        System.out.println("Default database with admin account administrator:administrator created");
    }
}
