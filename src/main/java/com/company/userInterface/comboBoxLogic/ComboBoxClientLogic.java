package com.company.userInterface.comboBoxLogic;

import com.company.exceptions.WrapperException;
import com.company.persistence.TableClients;
import com.company.shared.Client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxClientLogic {

    private static List<Client> elements = new ArrayList<>();

    public static void updateComboBox(JComboBox comboBox) throws WrapperException {
        elements.clear();
        comboBox.removeAllItems();

        elements = TableClients.queryAllClients();

        for(Client current: elements) {
            comboBox.addItem(current);
        }
    }
}
