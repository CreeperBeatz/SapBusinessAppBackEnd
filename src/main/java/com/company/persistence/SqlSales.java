package com.company.persistence;

import com.company.exceptions.*;
import com.company.shared.Product;
import com.company.shared.SaleClientProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class SqlSales {

    public static final String TABLE_SALES = "sales";
    public static final String COLUMN_SALES_ID = "_id";
    public static final String COLUMN_SALES_SALESMAN = "salesman";
    public static final String COLUMN_SALES_CLIENT = "client";
    public static final String COLUMN_SALES_PRODUCT = "product";
    public static final String COLUMN_SALES_QUANTITY = "quantity";
    public static final String COLUMN_SALES_DISCOUNT = "discount";
    public static final String COLUMN_SALES_PRICE = "price";
    public static final String COLUMN_SALES_DATE = "date";
    public static final int INDEX_SALES_ID = 1;
    public static final int INDEX_SALES_SALESMAN = 2;
    public static final int INDEX_SALES_CLIENT = 3;
    public static final int INDEX_SALES_PRODUCT = 4;
    public static final int INDEX_SALES_QUANTITY = 5;
    public static final int INDEX_SALES_DISCOUNT = 6;
    public static final int INDEX_SALES_PRICE = 7;
    public static final int INDEX_SALES_DATE = 8;

    public static final String INSERT_SALE_PREP = "INSERT INTO " + TABLE_SALES +
            "(" + COLUMN_SALES_SALESMAN + ", " +
            COLUMN_SALES_CLIENT + ", " + COLUMN_SALES_PRODUCT + ", " +
            COLUMN_SALES_QUANTITY + ", " + COLUMN_SALES_DISCOUNT + ", " +
            COLUMN_SALES_PRICE + ", " + COLUMN_SALES_DATE + ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String DELETE_SALE_PREP = "DELETE FROM " + TABLE_SALES + " WHERE " +
            COLUMN_SALES_ID + " = ?";


    public static final String QUERY_SALE_BY_SALESMAN_PREP = "SELECT " + TABLE_SALES + "." + COLUMN_SALES_ID + ", " +
            SqlUsers.TABLE_USERS + "." + SqlUsers.COLUMN_USERS_USERNAME + ", " +
            SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_NAME + ", " +
            SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_NAME + ", " +
            TABLE_SALES + "." + COLUMN_SALES_QUANTITY + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DISCOUNT + ", " +
            TABLE_SALES + "." + COLUMN_SALES_PRICE + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DATE +
            " FROM " + TABLE_SALES + " INNER JOIN " + SqlProducts.TABLE_PRODUCTS +
            " ON " + SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_ID + " = " +
            TABLE_SALES + "." + COLUMN_SALES_PRODUCT + ", " +
            SqlClients.TABLE_CLIENTS + " ON " + SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_ID +
            " = " + TABLE_SALES + "." + COLUMN_SALES_CLIENT + ", " + SqlUsers.TABLE_USERS + " ON " + SqlUsers.TABLE_USERS +
            "." + SqlUsers.COLUMN_USERS_ID + " = " + TABLE_SALES + "." + COLUMN_SALES_SALESMAN +
            " WHERE " + TABLE_SALES + "." + COLUMN_SALES_SALESMAN + " = ?";

    public static final String QUERY_SALE_BY_DATE_PREP = "SELECT " + TABLE_SALES + "." + COLUMN_SALES_ID + ", " +
            SqlUsers.TABLE_USERS + "." + SqlUsers.COLUMN_USERS_USERNAME + ", " +
            SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_NAME + ", " +
            SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_NAME + ", " +
            TABLE_SALES + "." + COLUMN_SALES_QUANTITY + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DISCOUNT + ", " +
            TABLE_SALES + "." + COLUMN_SALES_PRICE + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DATE +
            " FROM " + TABLE_SALES + " INNER JOIN " + SqlProducts.TABLE_PRODUCTS +
            " ON " + SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_ID + " = " +
            TABLE_SALES + "." + COLUMN_SALES_PRODUCT + ", " +
            SqlClients.TABLE_CLIENTS + " ON " + SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_ID +
            " = " + TABLE_SALES + "." + COLUMN_SALES_CLIENT + ", " + SqlUsers.TABLE_USERS + " ON " + SqlUsers.TABLE_USERS +
            "." + SqlUsers.COLUMN_USERS_ID + " = " + TABLE_SALES + "." + COLUMN_SALES_SALESMAN +
            " WHERE " + TABLE_SALES + "." + COLUMN_SALES_DATE + " > ? AND " + TABLE_SALES + "." + COLUMN_SALES_DATE + " < ?";

    public static final String QUERY_ALL_SALES = "SELECT " + TABLE_SALES + "." + COLUMN_SALES_ID + ", " +
            SqlUsers.TABLE_USERS + "." + SqlUsers.COLUMN_USERS_USERNAME + ", " +
            SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_NAME + ", " +
            SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_NAME + ", " +
            TABLE_SALES + "." + COLUMN_SALES_QUANTITY + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DISCOUNT + ", " +
            TABLE_SALES + "." + COLUMN_SALES_PRICE + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DATE +
            " FROM " + TABLE_SALES + " INNER JOIN " + SqlProducts.TABLE_PRODUCTS +
            " ON " + SqlProducts.TABLE_PRODUCTS + "." + SqlProducts.COLUMN_PRODUCTS_ID + " = " +
            TABLE_SALES + "." + COLUMN_SALES_PRODUCT + ", " +
            SqlClients.TABLE_CLIENTS + " ON " + SqlClients.TABLE_CLIENTS + "." + SqlClients.COLUMN_CLIENTS_ID +
            " = " + TABLE_SALES + "." + COLUMN_SALES_CLIENT + ", " + SqlUsers.TABLE_USERS + " ON " + SqlUsers.TABLE_USERS +
            "." + SqlUsers.COLUMN_USERS_ID + " = " + TABLE_SALES + "." + COLUMN_SALES_SALESMAN;


    public static void insertSale(int salesman, int client, int productID, int quantity, double discount)
            throws WrapperException {

        Connection conn = Datasource.getInstance().getConn();
        PreparedStatement statementSale = Datasource.getInstance().getInsertSale();
        Product product = SqlProducts.queryProductByID(productID);

        //VALIDATION
        if(!SqlUsers.salesmanExists(salesman)) {
            throw new WrapperException(new UserDoesNotExistException(), "Salesman doesn't exist!");
        }
        if(product == null) { //getStockByID throws ProductNowExistsException
            throw new WrapperException(new ProductDoesNotExistException(), "Product doesn't exist!");
        }
        if(product.getStock() < quantity) {
            throw new WrapperException(new NotEnoughStockException(), "Not enough stock!");
        }
        if(!SqlClients.clientExists(client)) {
            throw new WrapperException(new ClientDoesNotExistException(), "Client doesn't exist");
        }
        if(quantity < 0) {
            throw new WrapperException(new IllegalArgumentException(),"Only positive numbers for quantity!");
        }
        if(discount < 0) {
            throw new WrapperException(new IllegalArgumentException(), "Only positive numbers for discount!");
        }

        //INSERTION
        try {
            conn.setAutoCommit(false); //initiating a transaction

            statementSale.setInt(1, salesman);
            statementSale.setInt(2, client);
            statementSale.setInt(3, productID);
            statementSale.setInt(4, quantity);
            statementSale.setDouble(5, discount);
            statementSale.setDouble(6, calculatePrice(product, discount, quantity));
            statementSale.setLong(7, System.currentTimeMillis());

            if(SqlProducts.changeProduct(productID, "",-1 , product.getStock()-quantity, -1, "", "") == 1) {
                statementSale.execute();
                SqlClients.addClientPurchase(client); //adding a purchase to the client ID
                conn.commit();
            } else {
                throw new SQLException(); //Performing rollback, since more than 1 row was affected
            }
        } catch (SQLException e) {
            //TODO change with logging
            e.printStackTrace();
            try {
                System.out.println("performing rollback"); //TODO change to logger
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("CRITICAL ERROR: couldn't rollback");
                Datasource.getInstance().close();
                exit(-1);
            }
        } finally {
            try {
                //Resetting default commit behavior
                conn.setAutoCommit(true);
            } catch (SQLException e3) {
                System.out.println("CRITICAL ERROR: couldn't reset autocommit");
                Datasource.getInstance().close();
                exit(-1);
            }
        }
    }

    /**
     * Only admin should have access to that, in order to delete corrupt sales
     * UNSAFE
     * @param id sale to be deleted
     */
    public static int deleteSale(int id) throws WrapperException{
        PreparedStatement statement = Datasource.getInstance().getDeleteSale();
        try {
            statement.setInt(1 , id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't delete user");
        }
    }

    /**
     *
     *
     * @param id id of the user whose sales you want to check
     * @return List of SaleClientProducts
     */
    public static List<SaleClientProduct> querySalesBySalesman(int id) throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getQuerySaleBySalesman();
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            return getListFromResultSet(results);
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't execute sale by salesman query");
        }
    }

    /**
     * If you give -1 as a param, lowest or highest value will be assigned
     *
     * @param fromDate begin date of the sale query in millis
     * @param toDate end date of the sale query in millis
     * @return list of SaleClientProducts
     */
    public static List<SaleClientProduct> querySalesByDate(long fromDate, long toDate) throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getQuerySaleByDate();
            statement.setLong(1, fromDate);
            statement.setLong(2, toDate);

            ResultSet results = statement.executeQuery();
            return getListFromResultSet(results);
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't execute sale by date query");
        }
    }

    public static List<SaleClientProduct> queryAllSales() throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryAllSales();
            ResultSet results = statement.executeQuery();

            return getListFromResultSet(results);

        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't execute all sales query");
        }
    }

    private static List<SaleClientProduct> getListFromResultSet(ResultSet results) throws SQLException {
        List<SaleClientProduct> query = new ArrayList<>();

        while(results.next()){
            SaleClientProduct currResult = new SaleClientProduct();
            currResult.setId(results.getInt(INDEX_SALES_ID));
            currResult.setDate(results.getLong(INDEX_SALES_DATE));
            currResult.setDiscount(results.getDouble(INDEX_SALES_DISCOUNT));
            currResult.setPrice(results.getDouble(INDEX_SALES_PRICE));
            currResult.setProductName(results.getString(INDEX_SALES_PRODUCT));
            currResult.setClientName(results.getString(INDEX_SALES_CLIENT));
            currResult.setSalesmanUsername(results.getString(INDEX_SALES_SALESMAN));
            currResult.setQuantity(results.getInt(INDEX_SALES_QUANTITY));

            query.add(currResult);
        }
        return query;
    }

    private static double calculatePrice(Product product, double discount, int quantity) {
        return (product.getPrice()*quantity) - (product.getPrice()*quantity*discount/100);
    }
}
