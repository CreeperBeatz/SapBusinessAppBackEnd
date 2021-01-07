package com.company.userInterface.comboBoxLogic;

import javax.swing.*;
import java.util.List;

public class ComboBoxUserTypeLogic {

    private static String[] userTypes = {"Administrator", "Salesman"};

    /**
     * Very primitive class for setting admin and salesman elements in a combobox
     * As userTypes are static, I've allowed to do a direct initialisation of the String array
     * Rework if adding more user types
     * @param comboBox element to be updated
     */
    public static void updateElements(JComboBox comboBox) {
        comboBox.removeAllItems();
        comboBox.addItem(userTypes[0]);
        comboBox.addItem(userTypes[1]);
    }
}
