package com.company.persistence;

import com.company.exceptions.ClientDoesNotExistException;
import com.company.exceptions.NotEnoughStockException;
import com.company.exceptions.ProductDoesNotExistException;
import com.company.exceptions.UserDoesNotExistException;
import com.company.shared.SaleUserProduct;

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


    public void insertSale(String salesman, int client, int product, int quantity, double discount, double price)
            throws NotEnoughStockException, UserDoesNotExistException, ProductDoesNotExistException, ClientDoesNotExistException {

        //VALIDATION
        if(!TableUsers.salesmanExists("salesman")) {
            throw new UserDoesNotExistException();
        }

        if(TableProducts.getStockByID(product) < quantity) { //getStockByID throws ProductNowExistsException
            throw new NotEnoughStockException();
        }

        if(!TableClients.clientExists(client)) {
            throw new ClientDoesNotExistException();
        }

        //INSERTION
        //TODO transaction that inserts new sale and modifies quantity in the same time
    }
    public void deleteSale(){}; //maybe only admin having access
    public void changeSale(){}; //Maybe protection only if it's the last one

    private void querySalesmanByID(int ID) {

    }

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
     * @param fromDate begin date of the sale, if = -1, fromDate = 0
     * @param toDate end date of the sale, if = -1, toDate = currDate
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
}
