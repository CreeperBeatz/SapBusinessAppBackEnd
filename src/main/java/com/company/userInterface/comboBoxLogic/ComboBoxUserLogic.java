package com.company.userInterface.comboBoxLogic;

import com.company.persistence.SqlUsers;
import com.company.shared.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxUserLogic {

    private static List<User> elements = new ArrayList<>();

    public static void updateElements(JComboBox comboBox) {
        comboBox.removeAllItems();
        elements = SqlUsers.queryAllUsers();

        for(User current: elements) {
            comboBox.addItem(current);
        }
    }

}
