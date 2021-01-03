package com.company.userInterface;

import com.company.exceptions.WrapperException;
import com.company.persistence.Datasource;
import com.company.persistence.TableUsers;
import com.company.shared.User;

import javax.swing.*;
import java.awt.event.*;

public class LoginScreen extends Thread {
    private JButton loginButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPanel panelLogin;

    public LoginScreen() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                char[] passwordRaw = passwordField1.getPassword();
                String password = new String(passwordRaw);
                try {
                    User user = TableUsers.queryUserByUsernamePassword(username , password);

                    //If user doesn't exist
                    if (user == null) {
                        PopupCatalog.userDoesntExist();
                    }
                    else { //if user exists
                        switch (user.getType()){
                            case TableUsers.INDEX_ADMIN:
                                AdminScreen adminScreen =
                                        new AdminScreen(user.getId(), user.getUsername(), user.getEmail());
                                adminScreen.run();
                                break;
                            case TableUsers.INDEX_SALESMAN:
                                SalesmanScreen salesmanScreen =
                                        new SalesmanScreen(user.getId(), user.getUsername(), user.getEmail());
                                salesmanScreen.run();
                                break;
                            default:
                                PopupCatalog.unrecognisedUserType();
                                break;
                        }

                    }
                } catch (WrapperException e1) {
                    System.out.println(e1.getWrapperMessage()); //change to debugger?
                }
            }
        });


    }

    /**
     * Sets up the login page and keeps it active
     */
    public void run() {
        JFrame login = new JFrame("LoginScreen");
        login.setContentPane(new LoginScreen().panelLogin);
        login.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        login.addWindowListener(exitAppWindowAdapter());
        login.pack();
        login.setVisible(true);

        login.setLocationRelativeTo(null);

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