package com.company.model;

import com.company.exceptions.InvalidTypeException;
import com.company.exceptions.UserDoesNotExistException;
import com.company.utilities.MD5Hash;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TableUsers {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "_id";
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

    //insert new record prep
    public static final String INSERT_NEW_USER_PREP = "INSERT INTO " + TABLE_USERS + "(" +
            COLUMN_USERS_USERNAME + ", " + COLUMN_USERS_EMAIL + ", " + COLUMN_USERS_PASSWORD_HASH +
            ", " + COLUMN_USERS_TYPE + ") values(?, ?, ?, ?)";

    //delete record prep
    public static final String DELETE_USER_PREP = "DELETE FROM " + TABLE_USERS + " WHERE " + TABLE_USERS +
            "." + COLUMN_USERS_ID + " = ?";

    //change record prep
    public static final String CHANGE_USER_PREP = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_USERNAME +
            " = ?, " + COLUMN_USERS_EMAIL + " = ?, " + COLUMN_USERS_PASSWORD_HASH + " = ? , " + COLUMN_USERS_TYPE +
            " = ? WHERE " + COLUMN_USERS_ID + " = ?";

    //query user by id
    public static final String QUERY_USER_BY_ID_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_ID + " = ?";

    //query all users
    public static final String QUERY_ALL_USERS = "SELECT * FROM " + TABLE_USERS + " ORDER BY " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE;

    //query all traders
    public static final String QUERY_ALL_TRADERS = "SELECT * FROM " + TABLE_USERS + " WHERE " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE + " = " + INDEX_TRADER;


    /**
     * when given the following 4 parameters, method validates them via Regex, if not valid, function prints which username and on what
     * part the program failed. If all info is valid, method sets up prepared method with params, them executes SQL statement
     * @param username Saved as String. No special requirements.
     * @param password as in plain text, method utilizes MD5Hash to hash it inside.
     *                Minimum eight characters, at least one letter and one number.
     * @param email must contain word, followed by @, followed by word, followed by . , followed by 2-4 chars
     * @param userType must be lower  or queal to NUM_TYPES_USERS
     */
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
            Datasource.getInstance().getInsertUserPrep().setString(1 , username);
            Datasource.getInstance().getInsertUserPrep().setString(2, email);
            Datasource.getInstance().getInsertUserPrep().setString(3, MD5Hash.getHash(password));
            Datasource.getInstance().getInsertUserPrep().setString(4, Integer.toString(userType)); //TODO might change later, cuz spaghetti

            Datasource.getInstance().getInsertUserPrep().execute();
        } catch (SQLException e) {
            System.out.println("Couldn't insert user! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes an user from given unique id number
     * @param id unique number from the Users table, _id Column
     */
    public static void deleteUser(int id){
        try {
            Datasource.getInstance().getDeleteUserPrep().setInt(1, id);
            Datasource.getInstance().getDeleteUserPrep().execute();
        } catch (SQLException e) {
            System.out.println("Couldn't delete user - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Changes parameters of user._id
     * If a string is empty "" or int is 0, old value is preserved
     * @param id User that you want to change
     * @param username to be replaced. If you want to avoid changing it, put "" on username
     * @param email to be replaced. If you want to avoid changing it, put "" on email
     * @param password to be replaced. If you want to avoid changing it, put "" on password
     * @param type to be replaced. If you want to avoid changing it, put 0 on type
     */
    public static void changeUser(int id, String username, String email, String password, int type){
     try {
         //Validation
         if(type > NUM_TYPES_USERS || type < 0) {
             throw new InvalidTypeException();
         }

         Datasource.getInstance().getQueryUserByID().setInt(1, id);
         ResultSet result = Datasource.getInstance().getQueryUserByID().executeQuery();

         //ResultSet is initially set to -1, so we don't lose the first result by calling .next()
         if(!result.next()) {
             throw new UserDoesNotExistException();
         }

         Datasource.getInstance().getChangeUserPrep().setInt(5, id);

         //TODO split into methods, so code isn't duped
         //Checking and setting all params for the user
         if(username.equals("")) {
             Datasource.getInstance().getChangeUserPrep().setString(1, result.getString(INDEX_USERS_USERNAME));
         } else {
             Datasource.getInstance().getChangeUserPrep().setString(1, username);
         }

         if(email.equals("")) {
             Datasource.getInstance().getChangeUserPrep().setString(2, result.getString(INDEX_USERS_EMAIL));
         } else {
             Datasource.getInstance().getChangeUserPrep().setString(2, email);
         }

         if(password.equals("")) {
             Datasource.getInstance().getChangeUserPrep().setString(3, result.getString(INDEX_USERS_PASSWORD_HASH));
         } else {
             Datasource.getInstance().getChangeUserPrep().setString(3, MD5Hash.getHash(MD5Hash.getHash(password)));
         }

         if(type == 0) {
             Datasource.getInstance().getChangeUserPrep().setInt(4, result.getInt(INDEX_USERS_TYPE));
         } else {
             Datasource.getInstance().getChangeUserPrep().setInt(4, type);
         }

         //Writing the changes
         Datasource.getInstance().getChangeUserPrep().execute();

     } catch (SQLException e) {
         System.out.println("Couldn't change user -" + e.getMessage());
         e.printStackTrace();
     } catch (InvalidTypeException e) {
         System.out.println("Couldn't change user -" + e.getMessage());
         e.printStackTrace();
     } catch (UserDoesNotExistException e) {
         System.out.println("Couldn't change user -" + e.getMessage());
         e.printStackTrace();
     }
    }

    //////////////////////////////////////////////////////////
    //////////////////////////QUERIES/////////////////////////
    //////////////////////////////////////////////////////////

    /**
     * Query that utilizes the DataSource class and the connection in it
     * @return ArrayList of User, containing ID, Username, Email, Type of user WITHOUT password hash!
     */
    public static List<User> queryAllUsers() {//TODO change queries to prep statements if performance is better
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
    public static List<User> queryTraders() {//TODO change queries to prep statements if performance is better
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
