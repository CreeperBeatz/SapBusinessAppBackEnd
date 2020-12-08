package com.company;

import com.company.persistence.Datasource;
import com.company.persistence.TableUsers;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Datasource datasource = Datasource.getInstance();

        System.out.println(System.currentTimeMillis());

       datasource.open();

        //List<User> Users = TableUsers.queryTraders();
        //for(User user: Users) {
        //    System.out.println(user.getId() + "|" + user.getUsername()+"|" + user.getEmail() + "|" + user.getType());
        //}

        //TableUsers.insertUser("TestUser", "silnaParola123", "gp@kaisa.com", 1);
        TableUsers.deleteUser("TestUser");

        //TableUsers.changeUser("gpPochivka", "", "", "Administrator123", 0);

       datasource.close();

    }
}
