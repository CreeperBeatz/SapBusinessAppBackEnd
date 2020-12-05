package com.company.model;

public class TableProducts {

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTS_ID = "_id";
    public static final String COLUMN_PRODUCTS_NAME = "name";
    public static final String COLUMN_PRODUCTS_PRICE = "price";
    public static final String COLUMN_PRODUCTS_DISCOUNT = "discount";
    public static final String COLUMN_PRODUCTS_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCTS_IMAGE_URL = "imageUrl";
    public static final int INDEX_PRODUCTS_ID = 1;
    public static final int INDEX_PRODUCTS_NAME = 2;
    public static final int INDEX_PRODUCTS_PRICE = 3;
    public static final int INDEX_PRODUCTS_DISCOUNT = 4;
    public static final int INDEX_PRODUCTS_DESCRIPTION = 5;
    public static final int INDEX_PRODUCTS_IMAGE_URL = 6;

    //TODO query by price
    //TODO query by name
    public static final String QUERY_BY_PRICE_PREP = "";
    public static final String QUERY_BY_NAME_PREP = "";

    public void insertProduct(){};
    public void deleteProduct(){};
    public void changeProduct(){};


}
