package com.company.userInterface;

import com.company.persistence.Datasource;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminScreen extends Thread{
    private int id;
    private String username;
    private String email;

    private JPanel panelAdmin;
    private JTabbedPane tabbedPane1;

    public AdminScreen(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public void run() {
        JFrame adminScreen = new JFrame("Welcome " + this.username);
        adminScreen.setContentPane(this.panelAdmin);
        adminScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        adminScreen.addWindowListener(exitAppWindowAdapter());
        adminScreen.pack();
        adminScreen.setVisible(true);

        adminScreen.setLocationRelativeTo(null);
    }

    private WindowAdapter exitAppWindowAdapter() {
        WindowAdapter exit_application = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();

                int result = JOptionPane.showConfirmDialog(
                        frame ,
                        "Are you sure you want to exit the application?" ,
                        "Exit Application" ,
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    Datasource.getInstance().close();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        };
        return exit_application;
    }
}
