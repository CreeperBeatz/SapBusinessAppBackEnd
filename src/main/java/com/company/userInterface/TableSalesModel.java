package com.company.userInterface;

import com.company.exceptions.WrapperException;
import com.company.persistence.TableSales;
import com.company.shared.Sale;
import com.company.shared.SaleClientProduct;
import com.company.utilities.TimeConverter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TableSalesModel extends AbstractTableModel {

    private static String[] columnNames = {TableSales.COLUMN_SALES_ID, TableSales.COLUMN_SALES_SALESMAN,
            TableSales.COLUMN_SALES_CLIENT, TableSales.COLUMN_SALES_PRODUCT, TableSales.COLUMN_SALES_QUANTITY,
            TableSales.COLUMN_SALES_DISCOUNT, TableSales.COLUMN_SALES_PRICE, TableSales.COLUMN_SALES_DATE};

    private Object[][] data;

    //All sales
    public TableSalesModel(){
        data = getAllSales();
    }

    //Sale by date
    public TableSalesModel(long from, long to) {
        data = getSaleByDate(from, to);
    }

    //TODO Sale by trader, if needed

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

    private Object[][] getAllSales(){
        try{
            List<SaleClientProduct> sales = TableSales.queryAllSales();
            return getObjectFromListSales(sales);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getSaleByDate(long from, long to) {
        try {
            List<SaleClientProduct> sales = TableSales.querySalesByDate(from, to);
            return getObjectFromListSales(sales);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getObjectFromListSales(List<SaleClientProduct> queryData){
        Object[][] sales;

        //Prevents bricking the table if data = null
        if(queryData.size() == 0) {
            sales = new Object[1][columnNames.length];
            for(int i = 0; i < columnNames.length; i++) {
                sales[0][i] = "";
            }
            return sales;
        }

        sales = new Object[queryData.size()][columnNames.length];
        int j = 0;
        for(SaleClientProduct current : queryData) {
            sales[j][0] = current.getId();
            sales[j][1] = current.getSalesmanUsername();
            sales[j][2] = current.getClientName();
            sales[j][3] = current.getProductName();
            sales[j][4] = current.getQuantity();
            sales[j][5] = Double.toString(current.getDiscount()) + '%';
            sales[j][6] = current.getPrice();
            sales[j][7] = TimeConverter.MillisToString(current.getDate());
            j++;
        }
        return sales;
    }

    public static void setHeaders(JTable table){
        for(int i = 0; i< columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);

        }
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getColumnModel().getColumn(4).setMaxWidth(55);
        table.getColumnModel().getColumn(5).setMaxWidth(55);
        table.getColumnModel().getColumn(6).setMaxWidth(55);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
    }
}
