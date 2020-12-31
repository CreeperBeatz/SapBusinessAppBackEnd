package com.company;

import com.company.exceptions.ProductDoesNotExistException;
import com.company.exceptions.WrapperException;
import com.company.persistence.*;
import com.company.shared.Client;
import com.company.shared.Product;
import com.company.shared.User;


import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Datasource datasource = Datasource.getInstance();

        //System.out.println(System.currentTimeMillis());

       datasource.open();

        //List<User> Users = TableUsers.queryTraders();
        //for(User user: Users) {
        //    System.out.println(user.getId() + "|" + user.getUsername()+"|" + user.getEmail() + "|" + user.getType());
        //}

        //TableUsers.insertUser("TestUser", "silnaParola123", "gp@kaisa.com", 1);
        //TableUsers.deleteUser("TestUser");

        //TableUsers.changeUser("gpPochivka", "", "", "Administrator123", 0);
        //System.out.println(TableUsers.traderExists("gpPochivka"));

        //Product product = TableProducts.queryProductByID(2);
        //System.out.println(product.getId() + "|" + product.getStock() + "|" + product.getDiscount() + "|" + product.getPrice());

        //System.out.println(TableSales.deleteSale(5));

        //TableProducts.insertProduct("Samsung smart refrigerator", 599.99, 10, 0, "cools food to 3 degrees C", "default");

        try {
            TableClients.insertClient("iVaN", "iVaNoV", "plovdiv 1", "Bulgaria", "plovdiv", 5000);
            List<Client> clients = TableClients.queryClientByName("ivan");

            for(Client client : clients) {
                System.out.println(client.getName()+ "|" + client.getId());
            }
        }
        catch (WrapperException e) {

        }
       datasource.close();

    }
}
