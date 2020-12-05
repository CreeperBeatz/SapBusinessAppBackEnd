package com.company;

import com.company.model.Datasource;
import com.company.utilities.MD5Hash;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(MD5Hash.getHash("123456"));

       // datasource.open();
        //datasource.close();

    }
}
