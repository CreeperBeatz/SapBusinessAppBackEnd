package com.company.persistence;

import com.company.exceptions.WrapperException;
import com.company.shared.Client;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public static final int INDEX_CLIENTS_ID = 1;
    public static final int INDEX_CLIENTS_NAME = 2;
    public static final int INDEX_CLIENTS_SURNAME = 3;
    public static final int INDEX_CLIENTS_ADDRESS = 4;
    public static final int INDEX_CLIENTS_COUNTRY = 5;
    public static final int INDEX_CLIENTS_CITY = 6;
    public static final int INDEX_CLIENTS_POSTAL_CODE = 7;
    public static final int INDEX_CLIENTS_NUMBER_OF_PURCHASES = 8;

    public static final String INSERT_CLIENT_PREP = "INSERT INTO " + TABLE_CLIENTS + "(" + COLUMN_CLIENTS_NAME + ", " +
            COLUMN_CLIENTS_SURNAME + ", " + COLUMN_CLIENTS_ADDRESS +  ", " + COLUMN_CLIENTS_COUNTRY +  ", "
            + COLUMN_CLIENTS_CITY +  ", " + COLUMN_CLIENTS_POSTAL_CODE + ", " + COLUMN_CLIENTS_NUMBER_OF_PURCHASES + ") VALUES(?, ?, ?, ?, ?, ?, 0)";

    public static final String DELETE_CLIENT_PREP = "DELETE FROM " + TABLE_CLIENTS + " WHERE " + COLUMN_CLIENTS_ID +
            " = ?";

    public static final String CHANGE_CLIENT_PREP = "UPDATE " + TABLE_CLIENTS + " SET " + COLUMN_CLIENTS_NAME + " = ?, " +
            COLUMN_CLIENTS_SURNAME + " = ?, " + COLUMN_CLIENTS_ADDRESS + " = ?, " + COLUMN_CLIENTS_COUNTRY + " = ?, " +
            COLUMN_CLIENTS_CITY + " = ?, " + COLUMN_CLIENTS_POSTAL_CODE + " = ? WHERE " + COLUMN_CLIENTS_ID + " = ?";

    public static final String UPDATE_CLIENT_NUM_PURCHASES_PREP = "UPDATE " + TABLE_CLIENTS + " SET " + COLUMN_CLIENTS_NUMBER_OF_PURCHASES +
            " = " + COLUMN_CLIENTS_NUMBER_OF_PURCHASES + " + 1 WHERE " + COLUMN_CLIENTS_ID + " = ?";

    public static final String QUERY_ALL_CLIENTS_PREP = "SELECT * FROM " + TABLE_CLIENTS + " ORDER BY " +
            TABLE_CLIENTS + "." + COLUMN_CLIENTS_NAME + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENTS_SURNAME;

    public static final String QUERY_CLIENT_BY_NAME = "SELECT * FROM " + TABLE_CLIENTS + " WHERE UPPER(" +
            COLUMN_CLIENTS_NAME + ") = ? ORDER BY " + COLUMN_CLIENTS_SURNAME + " ASC";

    public static final String QUERY_CLIENT_BY_ID_PREP = "SELECT * FROM " + TABLE_CLIENTS + " WHERE " +
            TABLE_CLIENTS + "." + COLUMN_CLIENTS_ID + " = ?";


    public static void insertClient(String name, String surname, String address, String country, String city, int postalCode) throws WrapperException{
        if(postalCode < 1000) {
            throw new WrapperException(new NumberFormatException(), "Invalid postal code");
        }
        try {
            PreparedStatement statement = Datasource.getInstance().getInsertClient();

            statement.setString(1 , name);
            statement.setString(2, surname);
            statement.setString(3, address);
            statement.setString(4, country);
            statement.setString(5, city);
            statement.setInt(6, postalCode);

            statement.execute();
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't insert new client");
        }
    }

    public static void deleteClient(int id)throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getDeleteClient();
            statement.setInt(1, id);
            statement.executeUpdate(); //TODO transaction to check if only 1 row is deleted
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't delete client");
        }

    }

    /**
     *
     * @param id identification of the client
     * @param name if "" remains unchanged
     * @param surname if "" remains unchanged
     * @param address if "" remains unchanged
     * @param country if "" remains unchanged
     * @param city if "" remains unchanged
     * @param postalCode if <0 remains unchanged
     */
    public static void changeClient(int id, String name, String surname, String address, String country, String city,
                                    int postalCode) throws WrapperException{
        try {

            PreparedStatement statement = Datasource.getInstance().getChangeClient();
            Client client = queryClientByID(id);

            if(name.equals("")) {
                statement.setString(1, client.getName());
            } else {
                statement.setString(1 , name);
            }

            if(surname.equals("")){
                statement.setString(2, client.getSurname());
            } else {
                statement.setString(2 , surname);
            }

            if(address.equals("")){
                statement.setString(3, client.getAddress());
            } else {
                statement.setString(3 , address);
            }

            if(country.equals("")) {
                statement.setString(4, client.getCountry());
            } else {
                statement.setString(4 , country);
            }

            if(city.equals("")) {
                statement.setString(5, client.getCity());
            } else {
                statement.setString(5 , city);
            }

            if(postalCode < 0) {
                statement.setInt(6, client.getPostalCode());
            } else {
                statement.setInt(6 , postalCode);
            }

            statement.setInt(7, id);

            statement.executeUpdate(); //TODO maybe a transaction to check if there is more than 1 row affected?
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO end operation
        }
    }

    public static void addClientPurchase(int id){
        try {
            PreparedStatement statement = Datasource.getInstance().getUpdateClientPurchases();
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO end operation
            //Add client purchase is a method, only accessible from TableSales
            //It's not possible to have invalid client number, as it's being verified in TableSales
            //Therefore, SQL error means something is wrong on a technical level
        }
    }

    public static List<Client> queryAllClients() throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryAllClients();
            ResultSet results = statement.executeQuery();

            return getClientsFromResultSet(results);
        } catch (SQLException e) {
            throw new WrapperException(e);
        }
    }

    public static List<Client> queryClientByName(String name) throws WrapperException {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryClientByName();
            statement.setString(1 , name.toUpperCase());
            ResultSet results = statement.executeQuery();
            return getClientsFromResultSet(results);

        } catch (SQLException e) {
            throw new WrapperException(e);
        }
    }

    public static Client queryClientByID(int id) throws WrapperException {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryClientByID();
            statement.setInt(1 , id);
            ResultSet results = statement.executeQuery();

            List<Client> list = getClientsFromResultSet(results);
            //TODO if list.get(1)==null, throw client doesn't exist exception
            return list.get(0);

        } catch (SQLException e) {
            throw new WrapperException(e);
        }
    }

    private static List<Client> getClientsFromResultSet( ResultSet results) throws SQLException{

        List<Client> list = new ArrayList<>();

        while (results.next()) {
            Client client = new Client();

            client.setId(results.getInt(INDEX_CLIENTS_ID));
            client.setName(results.getString(INDEX_CLIENTS_NAME));
            client.setSurname(results.getString(INDEX_CLIENTS_SURNAME));
            client.setCountry(results.getString(INDEX_CLIENTS_COUNTRY));
            client.setCity(results.getString(INDEX_CLIENTS_CITY));
            client.setAddress(results.getString(INDEX_CLIENTS_ADDRESS));
            client.setPostalCode(results.getInt(INDEX_CLIENTS_POSTAL_CODE));
            client.setNumOfPurchases(results.getInt(INDEX_CLIENTS_NUMBER_OF_PURCHASES));

            list.add(client);
        }

        return list;
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
