package com.company.userInterface.comboBoxLogic;

import com.company.exceptions.WrapperException;
import com.company.persistence.SqlProducts;
import com.company.shared.Product;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxProductLogic {

    private static volatile List<Product> elements = new ArrayList<>();

    public static void updateElements() throws WrapperException {
        elements.clear();
        elements = SqlProducts.queryAllProducts();
    }

    public static final synchronized void setElements(JComboBox comboBox) {
        comboBox.removeAllItems();
        for(Product current: elements) {
            comboBox.addItem(current);
        }
    }
}
