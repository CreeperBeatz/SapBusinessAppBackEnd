package com.company.model;

import com.company.utilities.MD5Hash;

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


    public void insertUser(String username, String password, String email, byte userType){
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
        //TODO actual inserting of the data lol
    }

    void deleteUser(){};
    void changeUser(){};

    //TODO query all users
    //TODO query all traders?
}
