package com.company.persistence;

import com.company.exceptions.InvalidUserTypeException;
import com.company.exceptions.UserDoesNotExistException;
import com.company.exceptions.WrapperException;
import com.company.shared.VerificationSyntax;
import com.company.utilities.MD5Hash;
import com.company.shared.User;
import com.jcabi.log.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public static final int INDEX_SALESMAN = 2;

    //insert new user prep
    public static final String INSERT_NEW_USER_PREP = "INSERT INTO " + TABLE_USERS + "(" +
            COLUMN_USERS_USERNAME + ", " + COLUMN_USERS_EMAIL + ", " + COLUMN_USERS_PASSWORD_HASH +
            ", " + COLUMN_USERS_TYPE + ") values(?, ?, ?, ?)";

    //delete user prep
    public static final String DELETE_USER_PREP = "DELETE FROM " + TABLE_USERS + " WHERE " + TABLE_USERS +
            "." + COLUMN_USERS_ID + " = ?";

    //change user prep
    public static final String CHANGE_USER_PREP = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USERS_USERNAME +
            " = ?, " + COLUMN_USERS_EMAIL + " = ?, " + COLUMN_USERS_PASSWORD_HASH + " = ? , " + COLUMN_USERS_TYPE +
            " = ? WHERE " + COLUMN_USERS_USERNAME + " = ?";

    //query user by username
    public static final String QUERY_USER_BY_USERNAME_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_USERNAME + " = ?";

    //query user by username
    public static final String QUERY_USER_BY_USERNAME_HASH_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_USERNAME + " = ? AND " + COLUMN_USERS_PASSWORD_HASH + " = ?";

    //query user by username and type
    public static final String QUERY_USER_BY_USERNAME_TYPE_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_USERNAME + " = ? AND " + TABLE_USERS + "." + COLUMN_USERS_TYPE + " = ?";

    //query all users
    public static final String QUERY_ALL_USERS_PREP = "SELECT * FROM " + TABLE_USERS + " ORDER BY " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE;

    //query all traders
    public static final String QUERY_ALL_TRADERS_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE + " = " + INDEX_SALESMAN;



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

        try {
            if(!VerificationSyntax.verifyEmail(email)) {
                //TODO exception?
                System.out.println("invalid email @user " +  username);
                return;
            }

            if(VerificationSyntax.verifyPassword(password)){
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

            //TODO check if user already exists


            PreparedStatement insertUser = Datasource.getInstance().getInsertUserPrep();

            insertUser.setString(1 , username);
            insertUser.setString(2, email);
            insertUser.setString(3, password_hash);
            insertUser.setString(4, Integer.toString(userType));

            insertUser.execute();
        } catch (SQLException e) {
            System.out.println("Couldn't insert user! - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes an user from given unique username
     * @param id unique ID from the Users table
     */
    public static void deleteUser(int id){
        try {
            PreparedStatement deleteUser = Datasource.getInstance().getDeleteUserPrep();
            deleteUser.setInt(1, id);
            deleteUser.execute();
        } catch (SQLException e) {
            System.out.println("Couldn't delete user - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Changes parameters of user._id
     * If a string is empty "" or int is 0, old value is preserved
     * @param oldUsername User that you want to change
     * @param newUsername to be replaced. If you want to avoid changing it, put "" on username
     * @param email to be replaced. If you want to avoid changing it, put "" on email
     * @param password to be replaced. If you want to avoid changing it, put "" on password
     * @param type to be replaced. If you want to avoid changing it, put 0 on type
     */
    public static void changeUser(String oldUsername, String newUsername, String email, String password, int type){
     try {
         //Validation
         if(type > NUM_TYPES_USERS || type < 0) {
             throw new InvalidUserTypeException();
         }


         PreparedStatement queryUserByUsername = Datasource.getInstance().getQueryUserByUsername();
         PreparedStatement changeUser = Datasource.getInstance().getChangeUserPrep();

         queryUserByUsername.setString(1, oldUsername);
         ResultSet result = queryUserByUsername.executeQuery();

         //ResultSet is initially set to -1, so we don't lose the first result by calling .next()
         if(!result.next()) {
             throw new UserDoesNotExistException();
         }

         changeUser.setString(5, oldUsername);

         //Checking and setting all params for the user
         if(newUsername.equals("")) {
             changeUser.setString(1, result.getString(1));
         } else {
             changeUser.setString(1, newUsername);
         }

         if(email.equals("")) {
             changeUser.setString(2, result.getString(2));
         } else {
             changeUser.setString(2, email);
         }

         if(password.equals("")) {
             changeUser.setString(3, result.getString(3));
         } else {
             changeUser.setString(3, MD5Hash.getHash(MD5Hash.getHash(password)));
         }

         if(type == 0) {
             changeUser.setInt(4, result.getInt(4));
         } else {
             changeUser.setInt(4, type);
         }



         //Writing the changes
         changeUser.execute();

     } catch (SQLException e) {
         System.out.println("Couldn't change user -" + e.getMessage());
         e.printStackTrace();
     } catch (InvalidUserTypeException e) {
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
    public static List<User> queryAllUsers() {
        try {
            ResultSet results = Datasource.getInstance().getQueryAllUsers().executeQuery();

            return getUsersFromResultSet(results);

        } catch (SQLException e) {
            System.out.println("Couldn't execute query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Query that utilizes the DataSource class and the connection in it. Returns Traders only, without Admins
     *
     * @return ArrayList of User, containing ID, Username, Email, Type of user WITHOUT password hash!
     */
    public static List<User> querySalesmen() {//TODO change queries to prep statements if performance is better
        try {
            ResultSet results = Datasource.getInstance().getQueryAllTraders().executeQuery();
            return getUsersFromResultSet(results);
        } catch (SQLException e) {
            System.out.println("Couldn't execute query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean salesmanExists(String username) {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryUserByUsernameType();
            statement.setString(1 , username);
            statement.setInt(2, INDEX_SALESMAN);
            ResultSet results = statement.executeQuery();

            return results.next();
        } catch (SQLException e) { //If results.next throws an exception, it means there was no record
            System.out.println(e.getMessage());
            //TODO better error checking
            return false;
        }
    }

    public static User queryUserByUsernamePassword(String username, String password) throws WrapperException{
        password = MD5Hash.getHash(password);
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryUserByUsernameHash();
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet results = statement.executeQuery();

            List<User> query = getUsersFromResultSet(results);

            if(query.size()==0) {
                return null;
            } else if (query.size()>1){
                throw new SQLException("User not unique");
            } else {
                return query.get(0);//method returns a List, we only need the first(and only) element
            }
        } catch (SQLException e) {
           //TODO logger
            throw new WrapperException(e, "Couldn't execute username/password query!");
        }
    }

    private static List<User> getUsersFromResultSet(ResultSet results) throws SQLException {
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
    }
}