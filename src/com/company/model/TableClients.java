package com.company.model;

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

    public static final String QUERY_ALL_CLIENTS_PREP = "SELECT * FROM " + TABLE_CLIENTS + " ORDER BY " +
            TABLE_CLIENTS + "." + COLUMN_CLIENTS_NAME + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENTS_SURNAME;


    public void insertClient(){};
    //INSERT INTO CLIENTS(name, surname, address, country, city, postalCode, purchases)
    //VALUES ('proba', 'dae', 'nqkyde', 'Germany','Berlin',6000 , 0)
    public void deleteClient(){};
    public void changeClient(){};

    //TODO get all clients query
    //TODO get client by first name

}
