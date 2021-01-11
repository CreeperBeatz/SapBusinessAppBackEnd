package com.company.userInterface;

import com.company.persistence.*;

public class Main {

    public static void main(String[] args) {

        //Load an instance of the datasource
        Datasource datasource = Datasource.getInstance();
        datasource.open();

        //Load the login page of the GUI
        LoginScreen login = new LoginScreen();
        login.start();

        System.out.println(SqlUsers.CHANGE_USER_PREP);
    }
}
