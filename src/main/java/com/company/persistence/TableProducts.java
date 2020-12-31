package com.company.persistence;

import com.company.exceptions.ProductDoesNotExistException;
import com.company.exceptions.WrapperException;
import com.company.shared.Product;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    //insert product
    public static final String INSERT_PRODUCT_PREP = "INSERT INTO " + TABLE_PRODUCTS +
            "(" + COLUMN_PRODUCTS_NAME + ", " +
            COLUMN_PRODUCTS_PRICE + ", " + COLUMN_PRODUCTS_STOCK + ", " +
            COLUMN_PRODUCTS_DISCOUNT + ", " + COLUMN_PRODUCTS_DESCRIPTION + ", " +
            COLUMN_PRODUCTS_IMAGE_URL + ") VALUES(?, ?, ?, ?, ?, ?)";

    //delete product
    public static final String DELETE_PRODUCT_PREP = "DELETE FROM " + TABLE_PRODUCTS +
            " WHERE " + COLUMN_PRODUCTS_ID + " = ?";

    //change product
    public static final String CHANGE_PRODUCT_PREP = "UPDATE " + TABLE_PRODUCTS + " SET " +
            COLUMN_PRODUCTS_NAME + " = ?, " +
            COLUMN_PRODUCTS_PRICE + " = ?, " +
            COLUMN_PRODUCTS_STOCK + " = ?, " +
            COLUMN_PRODUCTS_DISCOUNT + " = ?, " +
            COLUMN_PRODUCTS_DESCRIPTION + " = ?, " +
            COLUMN_PRODUCTS_IMAGE_URL + " = ? WHERE " +
            COLUMN_PRODUCTS_ID + " = ?";

    //query by price
    public static final String QUERY_PRODUCT_BY_PRICE_PREP = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE + " > ? AND " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE + " < ?";

    //query by name, method should implement wild card
    public static final String QUERY_PRODUCT_BY_NAME_PREP = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + " LIKE ?";

    //query by id
    public static final String QUERY_PRODUCT_BY_ID_PREP = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
            TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + " = ?";

    public static void insertProduct(String name, double price, int stock, double discount, String description, String imgUrl){
        try {
            PreparedStatement statement = Datasource.getInstance().getInsertProduct();

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, stock);
            statement.setDouble(4, discount);
            statement.setString(5, description);
            statement.setString(6, imgUrl);

            statement.execute();
            //TODO verification

        } catch (SQLException e) {
            e.printStackTrace();
            //TODO logging
        }
    }

    public static void deleteProduct(int id) {
        Connection conn = Datasource.getInstance().getConn();
        PreparedStatement statement = Datasource.getInstance().getDeleteProduct();

        //Initiating transaction
        try {
            conn.setAutoCommit(false);
            statement.setInt(1, id);
            if(statement.executeUpdate() > 1) { //if affected rows > 1
                throw new SQLException();
            } else {
                conn.commit();
            }
        } catch (SQLException e) {
            //Reverting changes
            //TODO logging
            e.printStackTrace();
            try{
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
                //couldnt revert
                //TODO end program
            }
        } finally {
            //reseting autocommit
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                //TODO end program
            }
        }

    }

    /**
     * Change an existing product
     *
     * @param id Product to be changed
     * @param name if "", remains the same
     * @param price if <0, remains the same
     * @param stock if <0, remains the same
     * @param discount if <0, remains the same
     * @param description if "", remains the same
     * @param imgUrl if "", remains the same
     * @throws ProductDoesNotExistException product not found in database
     * @return number of rows affected. If > 1, there is a problem. returns -1 if there's an sql error
     */
    public static int changeProduct(int id, String name, double price, int stock, double discount, String description, String imgUrl)
        throws WrapperException {

        Product product = queryProductByID(id);
        PreparedStatement statement = Datasource.getInstance().getChangeProduct();

        if(product == null) throw new WrapperException(new ProductDoesNotExistException());


        try {
            if (!name.equals("")) {
                statement.setString(1 , name);
            } else {
                statement.setString(1, product.getName());
            }

            if (price >=0) {
                statement.setDouble(2, price);
            } else {
                statement.setDouble(2, product.getPrice());
            }

            if (stock > -1) {
                statement.setInt(3, stock);
            } else {
                statement.setInt(3, product.getStock());
            }

            if (discount >= 0) {
                statement.setDouble(4, discount);
            } else {
                statement.setDouble(4, product.getDiscount());
            }

            if (!description.equals("")) {
               statement.setString(5, description);
            } else {
                statement.setString(5, product.getDescription());
            }

            if (!imgUrl.equals("")) {
                statement.setString(6, imgUrl);
            } else {
                statement.setString(6, product.getImageUrl());
            }

            statement.setInt(7, id);
            return statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Couldnt execute changeProduct");
            e.printStackTrace();
            return -1;
            //TODO change to log
        }
    }


    public static List<Product> queryProductByName(String name) {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryProductByName();
            statement.setString(1,'%' + name + '%');
            ResultSet results = statement.executeQuery();
            List<Product> query = new ArrayList<>();

            while(results.next()) {
                Product product = new Product();

                product.setId(results.getInt(INDEX_PRODUCTS_ID));
                product.setName(results.getString(INDEX_PRODUCTS_NAME));
                product.setDescription(results.getString(INDEX_PRODUCTS_DESCRIPTION));
                product.setStock(results.getInt(INDEX_PRODUCTS_STOCK));
                product.setDiscount(results.getDouble(INDEX_PRODUCTS_DISCOUNT));
                product.setImageUrl(results.getString(INDEX_PRODUCTS_IMAGE_URL));

                query.add(product);
            }

            return query;

        } catch (SQLException e) {
            //TODO change with logging
            e.printStackTrace();
            return null;
        }
    }

    public static List<Product> queryProductByPrice(double lowPrice, double highPrice) {
        try {
            if(lowPrice > highPrice) {
                highPrice += lowPrice;
                lowPrice = highPrice - lowPrice;
                highPrice -= lowPrice;
            }
            PreparedStatement statement = Datasource.getInstance().getQueryProductByPrice();
            statement.setDouble(1, lowPrice);
            statement.setDouble(2, highPrice);
            ResultSet result = statement.executeQuery();

            List<Product> query = new ArrayList<>();

            while(result.next()){
                Product product = new Product();

                product.setId(result.getInt(INDEX_PRODUCTS_ID));
                product.setDescription(result.getString(INDEX_PRODUCTS_DESCRIPTION));
                product.setDiscount(result.getDouble(INDEX_PRODUCTS_DISCOUNT));
                product.setImageUrl(result.getString(INDEX_PRODUCTS_IMAGE_URL));
                product.setName(result.getString(INDEX_PRODUCTS_NAME));
                product.setPrice(result.getDouble(INDEX_PRODUCTS_PRICE));
                product.setStock(result.getInt(INDEX_PRODUCTS_STOCK));

                query.add(product);
            }

            return query;

        } catch (SQLException e) {
            //TODO logging
            e.printStackTrace();
            return null;
        }
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
