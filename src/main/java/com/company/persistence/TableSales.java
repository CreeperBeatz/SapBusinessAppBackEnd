package com.company.persistence;

import com.company.exceptions.ClientDoesNotExistException;
import com.company.exceptions.NotEnoughStockException;
import com.company.exceptions.ProductDoesNotExistException;
import com.company.exceptions.UserDoesNotExistException;
import com.company.shared.Product;
import com.company.shared.SaleUserProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableSales {

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


    public static final String QUERY_SALE_BY_TRADER_PREP = "SELECT " + TABLE_SALES + "." + COLUMN_SALES_ID + ", " +
            TABLE_SALES + "." + COLUMN_SALES_SALESMAN + ", " +
            TableClients.TABLE_CLIENTS + "." + TableClients.COLUMN_CLIENTS_NAME + ", " +
            TableProducts.TABLE_PRODUCTS + "." + TableProducts.COLUMN_PRODUCTS_NAME + ", " +
            TABLE_SALES + "." + COLUMN_SALES_QUANTITY + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DISCOUNT + ", " +
            TABLE_SALES + "." + COLUMN_SALES_PRICE + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DATE +
            " FROM " + TABLE_SALES + " INNER JOIN " + TableProducts.TABLE_PRODUCTS +
            " ON " + TableProducts.TABLE_PRODUCTS + "." + TableProducts.COLUMN_PRODUCTS_ID + " = " +
            TABLE_SALES + "." + COLUMN_SALES_PRODUCT + ", " +
            TableClients.TABLE_CLIENTS + " ON " + TableClients.TABLE_CLIENTS + "." + TableClients.COLUMN_CLIENTS_ID +
            " = " + TABLE_SALES + "." + COLUMN_SALES_CLIENT +
            " WHERE " + TABLE_SALES + "." + COLUMN_SALES_SALESMAN + " = ?";

    public static final String QUERY_SALE_BY_DATE_PREP = "SELECT " + TABLE_SALES + "." + COLUMN_SALES_ID + ", " +
            TABLE_SALES + "." + COLUMN_SALES_SALESMAN + ", " +
            TableClients.TABLE_CLIENTS + "." + TableClients.COLUMN_CLIENTS_NAME + ", " +
            TableProducts.TABLE_PRODUCTS + "." + TableProducts.COLUMN_PRODUCTS_NAME + ", " +
            TABLE_SALES + "." + COLUMN_SALES_QUANTITY + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DISCOUNT + ", " +
            TABLE_SALES + "." + COLUMN_SALES_PRICE + ", " +
            TABLE_SALES + "." + COLUMN_SALES_DATE +
            " FROM " + TABLE_SALES + " INNER JOIN " + TableProducts.TABLE_PRODUCTS +
            " ON " + TableProducts.TABLE_PRODUCTS + "." + TableProducts.COLUMN_PRODUCTS_ID + " = " +
            TABLE_SALES + "." + COLUMN_SALES_PRODUCT + ", " +
            TableClients.TABLE_CLIENTS + " ON " + TableClients.TABLE_CLIENTS + "." + TableClients.COLUMN_CLIENTS_ID +
            " = " + TABLE_SALES + "." + COLUMN_SALES_CLIENT +
            " WHERE " + TABLE_SALES + "." + COLUMN_SALES_DATE + " > ? AND " + TABLE_SALES + "." + COLUMN_SALES_DATE + " < ?";


    public void insertSale(String salesman, int client, int productID, int quantity, double discount)
            throws NotEnoughStockException, UserDoesNotExistException, ProductDoesNotExistException, ClientDoesNotExistException {

        Product product = TableProducts.queryProductByID(productID);

        //VALIDATION
        if(!TableUsers.salesmanExists("salesman")) {
            throw new UserDoesNotExistException();
        }


        if(product == null) { //getStockByID throws ProductNowExistsException
            throw new ProductDoesNotExistException();
        }

        if(product.getStock() < quantity) {
            throw new NotEnoughStockException();
        }

        if(!TableClients.clientExists(client)) {
            throw new ClientDoesNotExistException();
        }

        Connection conn = Datasource.getInstance().getConn();
        PreparedStatement statement = Datasource.getInstance().getInsertSale();

        //INSERTION
        try {
            conn.setAutoCommit(false);

            statement.setString(1, salesman);
            statement.setInt(2, client);
            statement.setInt(3, productID);
            statement.setInt(4, quantity);
            statement.setDouble(5, discount);
            statement.setDouble(6, calculatePrice(product, discount, quantity));
            statement.setLong(7, System.currentTimeMillis());

        } catch (SQLException e) {
            //TODO change with logging
            e.printStackTrace();
            try {
                System.out.println("performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("RED ALERT");
            }
        } finally {
            try {
                //Resetting default commit behavior
                conn.setAutoCommit(true);
            } catch (SQLException e3) {
                System.out.println("CRITICAL ERROR: couldn't reset autocommit");
            }
        }
        //TODO transaction that inserts new sale and modifies quantity in the same time
    }
    public void deleteSale(int id){}; //maybe only admin having access
    //TODO public void changeSale(){}; //Maybe protection only if it's the last one

    /**
     *
     *
     * @param username String of the Trader whose sales you want to check
     * @return
     */
    public List<SaleUserProduct> querySalesBySalesman(String username){
        try {
            PreparedStatement statement = Datasource.getInstance().getQuerySaleBySalesman();
            List<SaleUserProduct> query = new ArrayList<>();
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();

            return writeSaleUserProductsArray(results , query);
        } catch (SQLException e) {
            //TODO log
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * If you give -1 as a param, lowest or highest value will be assigned
     *
     * @param fromDate begin date of the sale query in millis
     * @param toDate end date of the sale query in millis
     * @return
     */
    public List<SaleUserProduct> querySalesByDate(long fromDate, long toDate){
        try {
            PreparedStatement statement = Datasource.getInstance().getQuerySaleBySalesman();
            List<SaleUserProduct> query = new ArrayList<>();

            statement.setLong(1, fromDate);
            statement.setLong(2, toDate);

            ResultSet results = statement.executeQuery();
            return writeSaleUserProductsArray(results , query);
        } catch (SQLException e) {
            //TODO log
            System.out.println(e.getMessage());
            return null;
        }
    }

    private List<SaleUserProduct> writeSaleUserProductsArray(ResultSet results , List<SaleUserProduct> query) throws SQLException {

        while(results.next()){
            SaleUserProduct currResult = new SaleUserProduct();
            currResult.setId(results.getInt(INDEX_SALES_ID));
            currResult.setDate(results.getInt(INDEX_SALES_DATE));
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

    private double calculatePrice(Product product, double discount, int quantity) {
        return (product.getPrice()*quantity) - (product.getPrice()*quantity*discount);
    }
}
