package com.company.persistence;

import com.company.exceptions.InvalidUserTypeException;
import com.company.exceptions.UserDoesNotExistException;
import com.company.exceptions.WrapperException;
import com.company.shared.VerificationSyntax;
import com.company.utilities.MD5Hash;
import com.company.shared.User;

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
            " = ? WHERE " + COLUMN_USERS_ID + " = ?";

    //query user by id
    public static final String QUERY_USER_BY_ID_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_ID + " = ?";

    //query user by username
    public static final String QUERY_USER_BY_USERNAME_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_USERNAME +
            " = ?";

    //query user by username and password
    public static final String QUERY_USER_BY_USERNAME_HASH_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_USERNAME + " = ? AND " + COLUMN_USERS_PASSWORD_HASH + " = ?";

    //query user by username and type
    public static final String QUERY_USER_BY_ID_TYPE_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." +
            COLUMN_USERS_ID + " = ? AND " + TABLE_USERS + "." + COLUMN_USERS_TYPE + " = ?";

    //query all users
    public static final String QUERY_ALL_USERS_PREP = "SELECT * FROM " + TABLE_USERS + " ORDER BY " +
            TABLE_USERS + "." + COLUMN_USERS_TYPE;

    //query all traders
    public static final String QUERY_ALL_SALESMEN_PREP = "SELECT * FROM " + TABLE_USERS + " WHERE " +
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
     * @param id User that you want to change
     * @param newUsername to be replaced. If you want to avoid changing it, put "" on username
     * @param email to be replaced. If you want to avoid changing it, put "" on email
     * @param password to be replaced. If you want to avoid changing it, put "" on password
     * @param type to be replaced. If you want to avoid changing it, put 0 on type
     */
    public static void changeUser(int id, String newUsername, String email, String password, int type){
         try {
             //Validation
             if(type > NUM_TYPES_USERS || type < 0) {
                 throw new InvalidUserTypeException();
             }


             PreparedStatement queryUserById = Datasource.getInstance().getQueryUserById();
             PreparedStatement changeUser = Datasource.getInstance().getChangeUserPrep();

             queryUserById.setInt(1, id);
             ResultSet result = queryUserById.executeQuery();
             User user = getUsersFromResultSet(result).get(0);

             changeUser.setInt(5, id);

             //Checking and setting all params for the user
             if(newUsername.equals("")) {
                 changeUser.setString(1, user.getUsername());
             } else {
                 changeUser.setString(1, newUsername);
             }

             if(email.equals("")) {
                 changeUser.setString(2, user.getEmail());
             } else {
                 changeUser.setString(2, email);
             }

             if(password.equals("")) {
                 changeUser.setString(3, user.getPassword_hash());
             } else {
                 changeUser.setString(3, MD5Hash.getHash(password));
             }

             if(type == 0) {
                 changeUser.setInt(4, user.getType());
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

    public static List<User> queryUsersByUsername(String username) throws WrapperException{
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryUserByUsername();
            statement.setString(1, username);

            ResultSet results = statement.executeQuery();
            return getUsersFromResultSet(results);
        } catch (SQLException e) {
            throw new WrapperException(e, "Couldn't execute user by username query");
        }
    }

    public static boolean salesmanExists(int id) {
        try {
            PreparedStatement statement = Datasource.getInstance().getQueryUserByIdType();
            statement.setInt(1 , id);
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