package com.company.persistence;

import com.company.utilities.MD5Hash;

import java.io.File;
import java.sql.*;

public class Datasource {

    //Database connection
    public static final String DB_NAME = "project.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\" + DB_NAME;

    //Database creation
    //TODO change all the fields (oh god) with the psfs above
    public static final String DATABASE_CREATION_STRING =
            "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
            "        \"username\"      TEXT,\n" +
            "        \"email\"      TEXT,\n" +
            "        \"hash\"  TEXT,\n" +
            "        \"type\"  INTEGER,\n" +
            "        PRIMARY KEY(\"username\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"sales\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"salesman\"      TEXT,\n" +
            "        \"client\"        INTEGER,\n" +
            "        \"product\"       INTEGER,\n" +
            "        \"quantity\"      INTEGER,\n" +
            "        \"discount\"      INTEGER,\n" +
            "        \"price\" INTEGER,\n" +
            "        \"date\" INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"products\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"name\"  TEXT,\n" +
            "        \"price\" NUMERIC,\n" +
            "        \"available\" INTEGER\n" +
            "        \"discount\"      NUMERIC,\n" +
            "        \"description\"   TEXT,\n" +
            "        \"imageUrl\"      TEXT,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"clients\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"name\"  TEXT,\n" +
            "        \"surname\"       TEXT,\n" +
            "        \"address\"       TEXT,\n" +
            "        \"country\"       TEXT,\n" +
            "        \"city\"  TEXT,\n" +
            "        \"postalCode\"    TEXT,\n" +
            "        \"purchases\"     INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "INSERT INTO users(username, email ,hash, type) VALUES('administrator', 'random@mail.com', '" +
            MD5Hash.getHash("administrator") +
            "', 1);";

    private Connection conn;

    //Users statements
    private PreparedStatement insertUserPrep;
    private PreparedStatement deleteUserPrep;
    private PreparedStatement changeUserPrep;
    private PreparedStatement queryUserByUsername;
    private PreparedStatement queryAllUsers;
    private PreparedStatement queryAllTraders;
    private PreparedStatement queryUserByUsernameType;

    //Sales statements
    private PreparedStatement querySaleBySalesman;
    private PreparedStatement querySaleByDate;

    //Products statements
    private PreparedStatement queryProductByID;
    private PreparedStatement queryProductByName;

    //Client statements
    private PreparedStatement queryClientByID;

    //Singleton design pattern, Thread safe
    private static Datasource instance = new Datasource();

    private Datasource(){}

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open(){

        boolean dbExists = DBExists();

        try {
           conn = DriverManager.getConnection(CONNECTION_STRING); //creates a .db file if not exists
           if(!dbExists) {
               createDB();
           }

           //Prepared statements
           insertUserPrep = conn.prepareStatement(TableUsers.INSERT_NEW_USER_PREP);
           deleteUserPrep = conn.prepareStatement(TableUsers.DELETE_USER_PREP);
           changeUserPrep = conn.prepareStatement(TableUsers.CHANGE_USER_PREP);
           queryUserByUsername = conn.prepareStatement(TableUsers.QUERY_USER_BY_USERNAME_PREP);
           queryAllUsers = conn.prepareStatement(TableUsers.QUERY_ALL_USERS_PREP);
           queryAllTraders = conn.prepareStatement(TableUsers.QUERY_ALL_TRADERS_PREP);
           queryUserByUsernameType = conn.prepareStatement(TableUsers.QUERY_USER_BY_USERNAME_TYPE_PREP);

           querySaleBySalesman = conn.prepareStatement(TableSales.QUERY_SALE_BY_TRADER_PREP);
           querySaleByDate = conn.prepareStatement(TableSales.QUERY_SALE_BY_DATE_PREP);

           queryProductByID = conn.prepareStatement(TableProducts.QUERY_PRODUCT_BY_ID_PREP);
           queryProductByName = conn.prepareStatement(TableProducts.QUERY_PRODUCT_BY_NAME_PREP);

           queryClientByID = conn.prepareStatement(TableClients.QUERY_CLIENT_BY_ID_PREP);


           return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            if(conn!=null) {
                //TODO close statements
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getQueryUserByUsernameType() {
        return  queryUserByUsernameType;
    }
    public PreparedStatement getQueryUserByUsername() {
        return queryUserByUsername;
    }
    public PreparedStatement getChangeUserPrep() {
        return changeUserPrep;
    }
    public PreparedStatement getDeleteUserPrep() {
        return deleteUserPrep;
    }
    public PreparedStatement getInsertUserPrep() {
        return insertUserPrep;
    }
    public PreparedStatement getQueryAllUsers() {
        return queryAllUsers;
    }
    public PreparedStatement getQueryAllTraders() {
        return queryAllTraders;
    }

    public PreparedStatement getQuerySaleBySalesman() {
        return querySaleBySalesman;
    }
    public PreparedStatement getQuerySaleByDate() {
        return querySaleByDate;
    }

    public PreparedStatement getQueryProductByID() {
        return queryProductByID;
    }
    public PreparedStatement getQueryProductByName() {
        return queryProductByName;
    }

    public PreparedStatement getQueryClientByID() {
        return queryClientByID;
    }

    public Connection getConn() {
        return conn;
    }

    private boolean DBExists() {
        File test = new File(DB_NAME);
        return test.exists();
    }

    private void createDB() throws SQLException{
        Statement statement = conn.createStatement();
        statement.execute(DATABASE_CREATION_STRING);
        statement.close();
        System.out.println("Default database with admin account administrator:administrator created");
    }
}