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

    public static void invalidDouble() {
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

    public static void invalidNumber() {
        JOptionPane.showMessageDialog(null,
                "Invalid number!\nPlease enter a Number in the Number fields!",
                "Invalid number",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void successfulSale() {
        JOptionPane.showMessageDialog(null,
                "Sale added successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean yesNoDeleteUser(String thingToDelete) {
        int result = JOptionPane.showConfirmDialog(null,
                "are you sure you want to delete this " + thingToDelete + "?",
                "Question",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}
