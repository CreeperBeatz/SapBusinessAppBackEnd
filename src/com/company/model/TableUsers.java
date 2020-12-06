package com.company.model;

import com.company.utilities.MD5Hash;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TableUsers {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "=_id";
    public static final String COLUMN_USERS_USERNAME = "username";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD_HASH = "hash";
    public static final String COLUMN_USERS_TYPE = "type";
    public static final int INDEX_USERS_ID = 1;
    public static final int INDEX_USERS_USERNAME = 2;
    public static final int INDEX_USERS_EMAIL = 3;
    public static final int INDEX_USERS_PASSWORD_HASH = 4;
    public static final int INDEX_USERS_TYPE = 5;

    public static final int NUM_TYPES_USERS = 2;
    public static final int INDEX_ADMIN = 1;
    public static final int INDEX_TRADER = 2;

    //INSERT INTO users(username, email, hash, type) VALUES("gosho", "mail.bg","3123j12j",2)

    //insert new record
    public static final String INSERT_NEW_USER_PREP = "INSERT INTO " + TABLE_USERS + "(" +
            COLUMN_USERS_USERNAME + ", " + COLUMN_USERS_EMAIL + ", " + COLUMN_USERS_PASSWORD_HASH +
            ", " + COLUMN_USERS_TYPE + ") values(?, ?, ?, ?)";

    //query all users
    public static final String QUERY_ALL_USERS = "SELECT * FROM " + TABLE_USERS + " ORDER BY " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE;

    //query all traders
    public static final String QUERY_ALL_TRADERS = "SELECT * FROM " + TABLE_USERS + " WHERE " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE + " = " + INDEX_TRADER;


    public static void insertUser(String username, String password, String email, int userType){
        String password_hash;

        //must contain word, followed by @, followed by word, followed by . , followed by 2-4 chars
        if(!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            System.out.println("invalid email @user " +  username);
            return;
        }

        //Minimum eight characters, at least one letter and one number:
        if(Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", password)){
            password_hash = MD5Hash.getHash(password);
        }
        else {
            System.out.println("invalid password @user " + username);
            return;
        }

        if(userType > NUM_TYPES_USERS) {
            System.out.println("invalid user type @user " + username);
            return;
        }

        try {
            Datasource.getInstance().getInsertUser().setString(1 , username);
            Datasource.getInstance().getInsertUser().setString(2, email);
            Datasource.getInstance().getInsertUser().setString(3, MD5Hash.getHash(password));
            Datasource.getInstance().getInsertUser().setString(4, Integer.toString(userType)); //TODO might change later, cuz spaghetti

            Datasource.getInstance().getInsertUser().execute();
        } catch (SQLException e) {
            System.out.println("Couldn't insert user! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteUser(){};
    public static void changeUser(){};

    //////////////////////////////////////////////////////////
    //////////////////////////QUERIES/////////////////////////
    //////////////////////////////////////////////////////////

    /**
     * Query that utilizes the DataSource class and the connection in it
     * @return ArrayList of User, containing ID, Username, Email, Type of user WITHOUT password hash!
     */
    public static List<User> queryAllUsers() {
        try(Statement statement = Datasource.getInstance().getConn().createStatement();
            ResultSet results = statement.executeQuery(QUERY_ALL_USERS)) {

            List<User> query = new ArrayList<>();
            while(results.next()) {
                User currUser = new User();
                currUser.setId(results.getInt(INDEX_USERS_ID));
                currUser.setUsername(results.getString(INDEX_USERS_USERNAME));
                currUser.setEmail(results.getString(INDEX_USERS_EMAIL));
                currUser.setType(results.getInt(INDEX_USERS_TYPE));

                query.add(currUser);
            }

            return query;

        } catch (SQLException e) {
            System.out.println("Couldn't execute query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Query that utilizes the DataSource class and the connection in it. Returns Traders only, without Admins
     * @return ArrayList of User, containing ID, Username, Email, Type of user WITHOUT password hash!
     */
    public static List<User> queryTraders() {
        try(Statement statement = Datasource.getInstance().getConn().createStatement();
            ResultSet results = statement.executeQuery(QUERY_ALL_TRADERS)) {

            List<User> query = new ArrayList<>();
            while(results.next()) {
                User currUser = new User();
                currUser.setId(results.getInt(INDEX_USERS_ID));
                currUser.setUsername(results.getString(INDEX_USERS_USERNAME));
                currUser.setEmail(results.getString(INDEX_USERS_EMAIL));
                currUser.setType(results.getInt(INDEX_USERS_TYPE));

                query.add(currUser);
            }

            return query;

        } catch (SQLException e) {
            System.out.println("Couldn't execute query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
