package com.company.userInterface.comboBoxLogic;

import com.company.exceptions.WrapperException;
import com.company.persistence.TableClients;
import com.company.persistence.TableProducts;
import com.company.shared.Client;
import com.company.shared.Product;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxProductLogic {

    public static List<Product> elements = new ArrayList<>();

    public static void updateElements(JComboBox<Product> comboBox) throws WrapperException {
        elements.clear();
        comboBox.removeAllItems();

        elements = TableProducts.queryAllProducts();

        for(Product current: elements) {
            comboBox.addItem(current);
        }
    }
}
