package com.company;

import com.company.exceptions.ProductDoesNotExistException;
import com.company.persistence.Datasource;
import com.company.persistence.TableProducts;
import com.company.persistence.TableSales;
import com.company.persistence.TableUsers;
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

        List<Product> products = TableProducts.queryProductByPrice(700, 0);

        for(Product e: products) {
            System.out.println(e.getId()+"|" + e.getName() + "|" + e.getPrice());
        }

       datasource.close();

    }
}
