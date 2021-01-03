package com.company.userInterface;

import com.company.persistence.*;


import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //Load an instance of the datasource
        Datasource datasource = Datasource.getInstance();
        datasource.open();

        //Load the login page of the GUI
        LoginScreen login = new LoginScreen();
        login.start();

        //TableUsers.insertUser("testTrader", "testTrader1", "test@bg.com", 2);
    }
}
