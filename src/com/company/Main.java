package com.company;

import com.company.model.Datasource;
import com.company.model.TableUsers;
import com.company.model.User;
import com.company.utilities.MD5Hash;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Datasource datasource = Datasource.getInstance();

        System.out.println(System.currentTimeMillis());

       datasource.open();

        List<User> Users = TableUsers.queryTraders();
        for(User user: Users) {
            System.out.println(user.getId() + "|" + user.getUsername()+"|" + user.getEmail() + "|" + user.getType());
        }

       datasource.close();

    }
}
