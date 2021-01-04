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
}
