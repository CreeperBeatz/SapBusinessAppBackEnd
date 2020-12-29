package com.company.persistence;

import com.company.shared.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableClients {
    public static final String TABLE_CLIENTS = "clients";
    public static final String COLUMN_CLIENTS_ID = "_id";
    public static final String COLUMN_CLIENTS_NAME = "name";
    public static final String COLUMN_CLIENTS_SURNAME = "surname";
    public static final String COLUMN_CLIENTS_ADDRESS = "address";
    public static final String COLUMN_CLIENTS_COUNTRY = "country";
    public static final String COLUMN_CLIENTS_CITY = "city";
    public static final String COLUMN_CLIENTS_POSTAL_CODE = "postalCode";
    public static final String COLUMN_CLIENTS_NUMBER_OF_PURCHASES = "purchases";

    //TODO method insert client
    public static final String INSERT_CLIENT_PREP = "INSERT INTO " + TABLE_CLIENTS + "(" + COLUMN_CLIENTS_NAME +
            COLUMN_CLIENTS_ADDRESS + COLUMN_CLIENTS_COUNTRY + COLUMN_CLIENTS_CITY + COLUMN_CLIENTS_POSTAL_CODE +
            ") VALUES(?, ?, ?, ?, ?, ?)";

    //TODO method delete client
    public static final String DELETE_CLIENT_PREP = "DELETE * FROM " + TABLE_CLIENTS + " WHERE " + COLUMN_CLIENTS_ID +
            " = ?";

    //TODO method update client
    public static final String CHANGE_CLIENT_PREP = "UPDATE " + TABLE_CLIENTS + " SET " + COLUMN_CLIENTS_NAME + " = ?, " +
            COLUMN_CLIENTS_SURNAME + " = ?, " + COLUMN_CLIENTS_ADDRESS + " = ?, " + COLUMN_CLIENTS_COUNTRY + " = ?, " +
            COLUMN_CLIENTS_CITY + " = ?, " + COLUMN_CLIENTS_POSTAL_CODE + " = ?, WHERE " + COLUMN_CLIENTS_ID + " = ?";

    //TODO method update purchases
    public static final String UPDATE_CLIENT_NUM_PURCHASES_PREP = "UPDATE " + TABLE_CLIENTS + " SET " + COLUMN_CLIENTS_NUMBER_OF_PURCHASES +
            " = " + COLUMN_CLIENTS_NUMBER_OF_PURCHASES + " + 1 WHERE " + COLUMN_CLIENTS_ID + " = ?";

    //TODO get all clients query
    public static final String QUERY_ALL_CLIENTS_PREP = "SELECT * FROM " + TABLE_CLIENTS + " ORDER BY " +
            TABLE_CLIENTS + "." + COLUMN_CLIENTS_NAME + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENTS_SURNAME;

    //TODO query by name method
    public static final String QUERY_CLIENT_BY_NAME = "SELECT * FROM " + TABLE_CLIENTS + " WHERE " +
            COLUMN_CLIENTS_NAME + " = ? ORDER BY ACS";

    public static final String QUERY_CLIENT_BY_ID_PREP = "SELECT * FROM " + TABLE_CLIENTS + " WHERE " +
            TABLE_CLIENTS + "." + COLUMN_CLIENTS_ID + " = ?";


    public void insertClient(){

    }

    public void deleteClient(){

    }

    public void changeClient(){

    }

    public void addClientPurchase(){

    }

    public Client queryAllClients(){
        return null;
    }

    public Client queryClientByName(String name){
        return null;
    }


    public static boolean clientExists(int id) {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryClientByID();
            statement.setInt(1 , id);
            ResultSet results = statement.executeQuery();

            if(results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            //TODO change with log
            e.printStackTrace();
            return false;
        }
    }

}
