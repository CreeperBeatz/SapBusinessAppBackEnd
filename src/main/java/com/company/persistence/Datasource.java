package com.company.persistence;

import com.company.userInterface.PopupCatalog;

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
    private PreparedStatement queryUserById;
    private PreparedStatement queryAllUsers;
    private PreparedStatement queryAllTraders;
    private PreparedStatement queryUserByIdType;
    private PreparedStatement queryUserByUsernameHash;
    private PreparedStatement queryUserByUsername;

    //Sales statements
    private PreparedStatement querySaleBySalesman;
    private PreparedStatement querySaleByDate;
    private PreparedStatement insertSale;
    private PreparedStatement deleteSale;
    private PreparedStatement queryAllSales;

    //Products statements
    private PreparedStatement queryProductByID;
    private PreparedStatement queryProductByName;
    private PreparedStatement changeProduct;
    private PreparedStatement queryProductByPrice;
    private PreparedStatement deleteProduct;
    private PreparedStatement insertProduct;
    private PreparedStatement queryAllProducts;

    //Client statements
    private PreparedStatement queryClientByID;
    private PreparedStatement queryClientByName;
    private PreparedStatement queryAllClients;
    private PreparedStatement insertClient;
    private PreparedStatement deleteClient;
    private PreparedStatement changeClient;
    private PreparedStatement updateClientPurchases;
    private PreparedStatement countNumClients;

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
           insertUserPrep = conn.prepareStatement(SqlUsers.INSERT_NEW_USER_PREP);
           deleteUserPrep = conn.prepareStatement(SqlUsers.DELETE_USER_PREP);
           changeUserPrep = conn.prepareStatement(SqlUsers.CHANGE_USER_PREP);
           queryUserById = conn.prepareStatement(SqlUsers.QUERY_USER_BY_ID_PREP);
           queryAllUsers = conn.prepareStatement(SqlUsers.QUERY_ALL_USERS_PREP);
           queryAllTraders = conn.prepareStatement(SqlUsers.QUERY_ALL_SALESMEN_PREP);
           queryUserByIdType = conn.prepareStatement(SqlUsers.QUERY_USER_BY_ID_TYPE_PREP);
           queryUserByUsernameHash = conn.prepareStatement(SqlUsers.QUERY_USER_BY_USERNAME_HASH_PREP);
           queryUserByUsername = conn.prepareStatement(SqlUsers.QUERY_USER_BY_USERNAME_PREP);

           //table sales
           querySaleBySalesman = conn.prepareStatement(SqlSales.QUERY_SALE_BY_SALESMAN_PREP);
           querySaleByDate = conn.prepareStatement(SqlSales.QUERY_SALE_BY_DATE_PREP);
           insertSale = conn.prepareStatement(SqlSales.INSERT_SALE_PREP);
           deleteSale = conn.prepareStatement(SqlSales.DELETE_SALE_PREP);
           queryAllSales = conn.prepareStatement(SqlSales.QUERY_ALL_SALES);

           //table products
           queryProductByID = conn.prepareStatement(SqlProducts.QUERY_PRODUCT_BY_ID_PREP);
           queryProductByName = conn.prepareStatement(SqlProducts.QUERY_PRODUCT_BY_NAME_PREP);
           changeProduct = conn.prepareStatement(SqlProducts.CHANGE_PRODUCT_PREP);
           queryProductByPrice = conn.prepareStatement(SqlProducts.QUERY_PRODUCT_BY_PRICE_PREP);
           deleteProduct = conn.prepareStatement(SqlProducts.DELETE_PRODUCT_PREP);
           insertProduct = conn.prepareStatement(SqlProducts.INSERT_PRODUCT_PREP);
           queryAllProducts = conn.prepareStatement(SqlProducts.QUERY_ALL_PRODUCTS);

           //table clients
           queryClientByID = conn.prepareStatement(SqlClients.QUERY_CLIENT_BY_ID_PREP);
           queryClientByName = conn.prepareStatement(SqlClients.QUERY_CLIENT_BY_NAME);
           queryAllClients = conn.prepareStatement(SqlClients.QUERY_ALL_CLIENTS_PREP);
           insertClient = conn.prepareStatement(SqlClients.INSERT_CLIENT_PREP);
           deleteClient = conn.prepareStatement(SqlClients.DELETE_CLIENT_PREP);
           changeClient = conn.prepareStatement(SqlClients.CHANGE_CLIENT_PREP);
           updateClientPurchases = conn.prepareStatement(SqlClients.UPDATE_CLIENT_NUM_PURCHASES_PREP);
           countNumClients = conn.prepareStatement(SqlClients.COUNT_NUM_RECORDS_PREP);

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
                if (queryUserById != null) queryUserById.close();
                if (queryAllUsers != null) queryAllUsers.close();
                if (queryAllTraders != null) queryAllTraders.close();
                if (queryUserByIdType != null) queryUserByIdType.close();
                if (queryUserByUsernameHash != null) queryUserByUsernameHash.close();
                if (queryUserByUsername != null) queryUserByUsername.close();

                //Table sales
                if (querySaleBySalesman != null) querySaleBySalesman.close();
                if (querySaleByDate != null) querySaleByDate.close();
                if (insertSale != null) insertSale.close();
                if (deleteSale != null) deleteSale.close();
                if (queryAllSales != null) queryAllSales.close();

                //Table products
                if (queryProductByID != null) queryProductByID.close();
                if (queryProductByName != null) queryProductByName.close();
                if (queryProductByPrice != null) queryProductByPrice.close();
                if (changeProduct != null) changeProduct.close();
                if (deleteProduct != null) deleteProduct.close();
                if (insertProduct != null) insertProduct.close();
                if (queryAllProducts != null) queryAllProducts.close();

                //Table clients
                if (queryClientByID != null) queryClientByID.close();
                if (queryAllClients != null) queryAllClients.close();
                if (queryClientByName != null) queryClientByName.close();
                if (insertClient != null) insertClient.close();
                if (deleteClient != null) deleteClient.close();
                if (changeClient != null) changeClient.close();
                if (updateClientPurchases != null) updateClientPurchases.close();
                if (countNumClients != null) countNumClients.close();

                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getQueryUserByIdType() {
        return queryUserByIdType;
    }
    public PreparedStatement getQueryUserById() {
        return queryUserById;
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
    public PreparedStatement getQueryUserByUsernameHash() {
        return queryUserByUsernameHash;
    }
    public PreparedStatement getQueryUserByUsername() {
        return queryUserByUsername;
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
    public PreparedStatement getQueryAllSales() {
        return queryAllSales;
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
    public PreparedStatement getQueryAllProducts() {
        return queryAllProducts;
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
    public PreparedStatement getCountNumClients() {
        return countNumClients;
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
        statement.execute(DBCreation.DATABASE_CREATION_USERS);
        statement.execute(DBCreation.DATABASE_CREATION_CLIENTS);
        statement.execute(DBCreation.DATABASE_CREATION_PRODUCTS);
        statement.execute(DBCreation.DATABASE_CREATION_SALES);
        statement.execute(DBCreation.DATABASE_CREATION_INSERT_ADMIN);
        statement.execute(DBCreation.DATABASE_CREATION_INSERT_SALESMAN);
        statement.close();
        //TODO Change with something more elegant
        PopupCatalog.successfulDBCreation();
    }
}
