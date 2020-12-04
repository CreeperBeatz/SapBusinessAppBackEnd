package com.company;

import com.company.model.Datasource;
import com.company.utilities.MD5Hash;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        Scanner scanner = new Scanner(System.in);

        System.out.println(MD5Hash.getHash("administrator"));

       // datasource.open();
        //datasource.close();

    }
}
