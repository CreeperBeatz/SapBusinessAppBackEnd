package com.company.model;

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

    //QUERY get transactions by trader
    //QUERY get transactions by date

    public void insertSale(){}; //TODO transaction that inserts new sale and modifies quantity in the same time
    public void deleteSale(){}; //maybe only admin having access
    public void changeSale(){}; //Maybe protection only if it's the last one
}
