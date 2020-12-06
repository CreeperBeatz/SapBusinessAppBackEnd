package com.company.model;

import com.company.utilities.MD5Hash;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.regex.Pattern;

public class Datasource {

    //Database connection
    public static final String DB_NAME = "project.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" +
            System.getProperty("user.dir") + "\\" + DB_NAME;

    //Database creation
    //TODO change all the fields (oh god) with the psfs above
    public static final String DATABASE_CREATION_STRING =
            "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"username\"      TEXT,\n" +
            "        \"email\"      TEXT,\n" +
            "        \"hash\"  TEXT,\n" +
            "        \"type\"  INTEGER,\n" +
            "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"sales\" (\n" +
            "        \"_id\"   INTEGER,\n" +
            "        \"salesman\"      INTEGER,\n" +
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

    //TODO prepared statements
    private PreparedStatement insertUser;

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
           insertUser = conn.prepareStatement(TableUsers.INSERT_NEW_USER_PREP);
           return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            if(conn!=null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PreparedStatement getInsertUser() {
        return insertUser;
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
