package com.company.userInterface;

import com.company.exceptions.WrapperException;
import com.company.persistence.TableProducts;
import com.company.shared.Product;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableProductsModel extends AbstractTableModel {

    private static String[] columnNames = {TableProducts.COLUMN_PRODUCTS_NAME, TableProducts.COLUMN_PRODUCTS_PRICE,
            TableProducts.COLUMN_PRODUCTS_STOCK,
            TableProducts.COLUMN_PRODUCTS_DESCRIPTION};

    private Object[][] data;

    //all products
    public TableProductsModel(){
        data = getAllProducts();
    }

    //products by name
    public TableProductsModel(String name){
        data = getProductByName(name);
    }

    //products by price
    public TableProductsModel(double from, double to){
        data = getProductsByPrice(from, to);
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex , int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    private Object[][] getAllProducts(){
        try {
            List<Product> products = TableProducts.queryAllProducts();
            return getObjectsFromProductList(products);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage() + "\n" + e.getMessage());
            return null;
        }
    }

    private Object[][] getProductsByPrice(double from, double to) {
        try {
            List<Product> products = TableProducts.queryProductByPrice(from, to);
            return getObjectsFromProductList(products);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getProductByName(String name) {
        try {
            List<Product> products = TableProducts.queryProductByName(name);
            return getObjectsFromProductList(products);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getObjectsFromProductList(List<Product> queryData) {
        Object[][] products;

        //bandage fix of table bricking when data = null
        if (queryData.size() == 0) {
            products = new Object[1][columnNames.length];
            for(int i = 0; i < columnNames.length; i++) {
                products[0][i] = "";
            }
            return products;
        }

        products = new Object[queryData.size()][columnNames.length];

        int j = 0;
        for (Product current : queryData) {
            products[j][0] = current.getName();
            products[j][1] = current.getPrice();
            products[j][2] = current.getStock();
            //products[j][3] = current.getDiscount(); //TODO extend functionality
            products[j][3] = current.getDescription();
            j++;
        }

        return products;
    }

    public static void setHeaders(JTable table) {
        for(int i = 0; i< columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);

        }
        table.getColumnModel().getColumn(1).setMaxWidth(55);
        table.getColumnModel().getColumn(2).setMaxWidth(55);
    }

}
