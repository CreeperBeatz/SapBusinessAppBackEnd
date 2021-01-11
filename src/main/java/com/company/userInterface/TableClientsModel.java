package com.company.userInterface;

import com.company.exceptions.WrapperException;
import com.company.persistence.SqlClients;
import com.company.shared.Client;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableClientsModel extends AbstractTableModel {

    private static String[] columnNames = {SqlClients.COLUMN_CLIENTS_NAME, SqlClients.COLUMN_CLIENTS_SURNAME,
            SqlClients.COLUMN_CLIENTS_ADDRESS, SqlClients.COLUMN_CLIENTS_COUNTRY, SqlClients.COLUMN_CLIENTS_CITY,
            SqlClients.COLUMN_CLIENTS_POSTAL_CODE, SqlClients.COLUMN_CLIENTS_NUMBER_OF_PURCHASES};

    private Object [][] data = {{"", "", "", "", "", 0, 0}};

    public TableClientsModel(String name) {
        data = getClientsByName(name);
    }

    public TableClientsModel(){
        data = getAllClients();
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

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public Object[][] getAllClients() {
        try {
            List<Client> queryData = SqlClients.queryAllClients();
            return getClientObjectsFromClientList(queryData, SqlClients.countRowClients(), columnNames.length);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }
    public static void setHeaders(JTable table) {
        for(int i = 0; i< columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
        }
    }

    public Object[][] getClientsByName(String name) {
        try {
            List<Client> queryData = SqlClients.queryClientByName(name);
            return getClientObjectsFromClientList(queryData, queryData.size() , columnNames.length);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getClientObjectsFromClientList(List<Client> queryData, int ySize, int xSize) {
        Object[][] clients;

        //Bandage fix, I dont have the time to see the origin of the issue
        //Prevents bricking the jTable if rowCount == 0
        if (ySize == 0) {
            clients = new Object[1][xSize];
            for(int i = 0; i < columnNames.length; i++) {
                clients[0][i] = "";
            }
            return clients;
        }

        clients = new Object[ySize][xSize];

        int j = 0; //very bandage, please dont kill me
        for(Client current:queryData) {
            clients[j][0] = current.getName();
            clients[j][1] = current.getSurname();
            clients[j][2] = current.getAddress();
            clients[j][3] = current.getCountry();
            clients[j][4] = current.getCity();
            clients[j][5] = current.getPostalCode();
            clients[j][6] = current.getNumOfPurchases();
            j++;
        }
        return clients;
    }
}
