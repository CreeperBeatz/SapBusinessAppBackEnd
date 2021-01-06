package com.company.userInterface;

import javax.swing.*;

public class PopupCatalog {

    public static void userDoesntExist() {
        JOptionPane.showMessageDialog(null,
                "Username or password doesn't match!",
                "No user found",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void unrecognisedUserType() {
        JOptionPane.showMessageDialog(null,
                "User type unrecognised!\nPlease contact a system administrator!",
                "Invalid user",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void customError(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "Invalid user",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void invalidNumber() {
        JOptionPane.showMessageDialog(null,
                "Number not valid!\n Please enter a double with . separator",
                "Invalid number",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void invalidDate() {
        JOptionPane.showMessageDialog(null,
                "Date not recognised!\nPlease follow the pattern!\n'dd.mm.yyyy'",
                "Invalid date",
                JOptionPane.ERROR_MESSAGE);
    }

}
