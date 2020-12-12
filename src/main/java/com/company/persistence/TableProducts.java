package com.company.persistence;

import com.company.exceptions.ProductDoesNotExistException;
import com.company.shared.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableProducts {

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTS_ID = "_id";
    public static final String COLUMN_PRODUCTS_NAME = "name";
    public static final String COLUMN_PRODUCTS_PRICE = "price";
    public static final String COLUMN_PRODUCTS_STOCK = "available";
    public static final String COLUMN_PRODUCTS_DISCOUNT = "discount";
    public static final String COLUMN_PRODUCTS_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCTS_IMAGE_URL = "imageUrl";
    public static final int INDEX_PRODUCTS_ID = 1;
    public static final int INDEX_PRODUCTS_NAME = 2;
    public static final int INDEX_PRODUCTS_PRICE = 3;
    public static final int INDEX_PRODUCTS_STOCK = 4;
    public static final int INDEX_PRODUCTS_DISCOUNT = 5;
    public static final int INDEX_PRODUCTS_DESCRIPTION = 6;
    public static final int INDEX_PRODUCTS_IMAGE_URL = 7;


    //query by price
    public static final String QUERY_PRODUCT_BY_PRICE_PREP = "";

    //query by name, method should implement wild card
    public static final String QUERY_PRODUCT_BY_NAME_PREP = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + " = ?";

    //query by id
    public static final String QUERY_PRODUCT_BY_ID_PREP = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + " = ?";

    public void insertProduct(){};
    //INSERT INTO PRODUCTS(name, price, available, discount, description, imageUrl)
    //VALUES ('cooler', 99.99, 14, 0, 'cools your room really well', 'https://4.imimg.com/data4/BV/LP/MY-4223299/air-cooler-500x500.jpg')
    public void deleteProduct(){};

    public void changeProduct(){}


    public List<Product> queryProductByName(String name) {
        return null;
    }

    public static int getStockByID(int id) throws ProductDoesNotExistException {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryProductByID();
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            if(!results.next()) {
                throw new ProductDoesNotExistException();
            }

            return results.getInt(INDEX_PRODUCTS_STOCK);
        } catch (SQLException e) {
            //TODO replace with log
            e.printStackTrace();
            return -1;
        }
    }

    public static Product queryProductByID(int id) {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryProductByID();
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if(!result.next()) {
                return null;
            } else {
                Product product = new Product();
                product.setId(id);
                product.setDescription(result.getString(INDEX_PRODUCTS_DESCRIPTION));
                product.setDiscount(result.getDouble(INDEX_PRODUCTS_DISCOUNT));
                product.setImageUrl(result.getString(INDEX_PRODUCTS_IMAGE_URL));
                product.setName(result.getString(INDEX_PRODUCTS_NAME));
                product.setPrice(result.getDouble(INDEX_PRODUCTS_PRICE));
                product.setStock(result.getInt(INDEX_PRODUCTS_STOCK));

                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
