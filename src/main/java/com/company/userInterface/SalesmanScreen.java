package com.company.userInterface;

import com.company.persistence.Datasource;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

public class SalesmanScreen extends Thread{
    private int id;
    private String username;
    private String email;


    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel Clients;
    private JTable tableClients;
    private JTextField textField1;
    private JButton clientsByNameButton;
    private JButton allClientsButton;
    private JTable tableSaleHistory;
    private JTextField textField2;
    private JButton saleBySalesmanButton;
    private JTextField textField4;
    private JTable table1;
    private JTextField textField3;
    private JTextField textField5;
    private JButton queryByPriceButton;
    private JTextField textField6;
    private JButton queryByNameButton;


    public SalesmanScreen(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    /**
     * Sets up the login page and keeps it active
     */
    public void run() {
        JFrame salesmanScreen = new JFrame("SalesmanScreen");
        salesmanScreen.setContentPane(this.panel1);
        salesmanScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        salesmanScreen.addWindowListener(exitAppWindowAdapter());
        salesmanScreen.pack();
        salesmanScreen.setVisible(true);

        salesmanScreen.setLocationRelativeTo(null);

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

    public void createUIComponents(){

    }
}
