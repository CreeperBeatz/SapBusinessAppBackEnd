package com.company.userInterface;

import com.company.exceptions.EmptyTextFieldException;
import com.company.exceptions.InvalidEmailException;
import com.company.exceptions.InvalidPasswordException;
import com.company.exceptions.WrapperException;
import com.company.persistence.*;
import com.company.shared.Client;
import com.company.shared.Product;
import com.company.shared.User;
import com.company.shared.VerificationSyntax;
import com.company.userInterface.comboBoxLogic.ComboBoxUserLogic;
import com.company.userInterface.comboBoxLogic.ComboBoxClientLogic;
import com.company.userInterface.comboBoxLogic.ComboBoxProductLogic;
import com.company.userInterface.comboBoxLogic.ComboBoxUserTypeLogic;
import com.company.utilities.TimeConverter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminScreen extends Thread {
    private final int id;
    private final String username;
    private final String email;


    private JPanel panelAdmin;
    private JTable jTableClients;
    private JTextField clientNameTextField;
    private JButton clientsByNameButton;
    private JButton allClientsButton;
    private JButton allSalesButton;
    private JTable jTableProducts;
    private JTextField fromProductTextField;
    private JTextField toProductTextField;
    private JButton queryByPriceButton;
    private JTextField productByNameTextField;
    private JButton queryByNameButton;
    private JTextField textFieldAddClientName;
    private JTextField textFieldAddClientSurname;
    private JTextField textFieldAddClientAddress;
    private JTextField textFieldAddClientCountry;
    private JTextField textFieldAddClientCity;
    private JTextField textFieldAddClientPostalCode;
    private JButton insertClientButton;
    private JTextField fromSalesTextField;
    private JTextField toSalesTextField;
    private JButton saleByDateButton;
    private JTable jTableSaleHistory;
    private JButton queryAllProductsButton;
    private JTextField textFieldAddProductName;
    private JTextField textFieldAddProductPrice;
    private JTextField textFieldAddProductStock;
    private JTextField textFieldAddProductDescription;
    private JTextField textFieldAddProductImageURL;
    private JButton insertProductButton;
    private JComboBox comboBoxModifyProduct;
    private JTextField textFieldChangeProductName;
    private JTextField textFieldChangeProductPrice;
    private JTextField textFieldChangeProductDesc;
    private JTextField textFieldChangeProductImg;
    private JButton changeProductButton;
    private JTextField textFieldChangeProductStock;
    private JLabel labelAddStock;
    private JLabel labelTotalPurchases;
    private JLabel labelAddSaleCurrentStock;
    private JComboBox comboBoxChangeClient;
    private JTextField textFieldChangeClientName;
    private JTextField textFieldChangeClientSurname;
    private JTextField textFieldChangeClientAddress;
    private JTextField textFieldChangeClientCountry;
    private JTextField textFieldChangeClientCity;
    private JTextField textFieldChangeClientPostalCode;
    private JButton changeClientButton;
    private JTabbedPane tabbedPane1;
    private JPanel clientsPanel;
    private JScrollPane JScrollPaneClients;
    private JPanel saleHistoryPanel;
    private JPanel productsPanel;
    private JPanel addClientPanel;
    private JPanel addProduct;
    private JPanel modifyProduct;
    private JTextField textFieldAddUserUsername;
    private JTextField textFieldAddUserPassword;
    private JTextField textFieldAddUserEmail;
    private JComboBox comboBoxAddUserType;
    private JButton addUserButton;
    private JButton deleteProductButton;
    private JButton deleteClientButton;
    private JComboBox comboBoxChangeUser;
    private JTextField textFieldChangeUserUsername;
    private JButton changeUserButton;
    private JButton deleteUserButton;
    private JTextField textFieldChangeUserPassword;
    private JTextField textFieldChangeUserEmail;
    private JComboBox comboBoxChangeUserType;
    private JTable jTableUsers;
    private JPanel usersjPanel;
    private JButton allUsersButton;
    private JTextField textFieldUserByUsername;
    private JButton userByUsernameButton;


    /**
     * Sets up the salesman page and keeps it active
     */
    public void run() {
        JFrame adminScreen = new JFrame("Welcome, " + username);
        adminScreen.setContentPane(this.panelAdmin);
        adminScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        adminScreen.addWindowListener(exitAppWindowAdapter());
        adminScreen.pack();
        adminScreen.setVisible(true);

        adminScreen.setLocationRelativeTo(null);
        createUIComponents();


    }

    public AdminScreen(int id , final String username , String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        clientsByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //DefaultTableModel model = (DefaultTableModel) tableClients.getModel();
                jTableClients.setModel(new TableClientsModel(clientNameTextField.getText()));
                TableClientsModel.setHeaders(jTableClients); //this is the only way I found to update headers... sorry
            }
        });
        allClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableClients.setModel(new TableClientsModel());
                TableClientsModel.setHeaders(jTableClients);
            }
        });
        queryAllProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableProducts.setModel(new TableProductsModel());
                TableProductsModel.setHeaders(jTableProducts);
            }
        });
        queryByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableProducts.setModel(new TableProductsModel(productByNameTextField.getText()));
                TableProductsModel.setHeaders(jTableProducts);
            }
        });
        queryByPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double from = Double.parseDouble(fromProductTextField.getText());
                    double to = Double.parseDouble(toProductTextField.getText());

                    jTableProducts.setModel(new TableProductsModel(from , to));
                    TableProductsModel.setHeaders(jTableProducts);
                } catch (NumberFormatException e1) {
                    PopupCatalog.invalidDouble();
                }
            }
        });
        allSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableSaleHistory.setModel(new TableSalesModel());
                TableSalesModel.setHeaders(jTableSaleHistory);
            }
        });
        saleByDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern pattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
                Matcher matcherFrom = pattern.matcher(fromSalesTextField.getText());
                Matcher matcherTo = pattern.matcher(toSalesTextField.getText());

                if (!matcherFrom.matches() || !matcherTo.matches()) {
                    PopupCatalog.invalidDate();
                    fromSalesTextField.setText("");
                    toSalesTextField.setText("");
                    return;
                }

                try {
                    long from = TimeConverter.StringToMillis(fromSalesTextField.getText());
                    long to = TimeConverter.StringToMillis(toSalesTextField.getText());
                    jTableSaleHistory.setModel(new TableSalesModel(from , to));
                    TableSalesModel.setHeaders(jTableSaleHistory);
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }


            }
        });
        insertClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = textFieldAddClientName.getText();
                    String surname = textFieldAddClientSurname.getText();
                    String address = textFieldAddClientAddress.getText();
                    String country = textFieldAddClientCountry.getText();
                    String city = textFieldAddClientCity.getText();
                    int postalCode = Integer.parseInt(textFieldAddClientPostalCode.getText());

                    if (name.equals("") || surname.equals("") || address.equals("") || country.equals("") || city.equals("")) {
                        throw new WrapperException(new EmptyTextFieldException() , "Please enter values on all the fields!");
                    } else if (postalCode < 0 || postalCode > 99999) {
                        throw new WrapperException(new NumberFormatException() , "Please enter a valid postal code!");
                    } else {
                        TableClients.insertClient(name , surname , address , country , city , postalCode);
                        //ComboBoxClientLogic.updateComboBox(comboBoxClient);//TODO automate this
                        ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
                    }
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }
            }
        });
        insertProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = textFieldAddProductName.getText();
                    double price = Double.parseDouble(textFieldAddProductPrice.getText());
                    int stock = Integer.parseInt(textFieldAddProductStock.getText());
                    double discount = 0; //TODO implement functionality
                    String description = textFieldAddProductDescription.getText();
                    String imgUrl = textFieldAddProductImageURL.getText();

                    if (name.equals("") || description.equals("") || imgUrl.equals("")) {
                        throw new WrapperException(new EmptyTextFieldException() , "Please enter information on all the fields!");
                    }
                    if (price < 0) {
                        textFieldAddProductPrice.setText("");
                        throw new WrapperException(new NumberFormatException() , "Price can't be lower than 0!");
                    }
                    if (stock < 0) {
                        textFieldAddProductStock.setText("");
                        throw new WrapperException(new NumberFormatException() , "Stock can't be lower than 0!");
                    }

                    TableProducts.insertProduct(name , price , stock , discount , description , imgUrl);

                    ComboBoxProductLogic.updateElements(); //TODO make this automatic
                    //ComboBoxProductLogic.setElements(comboBoxProduct);
                    ComboBoxProductLogic.setElements(comboBoxModifyProduct);

                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                } catch (NumberFormatException e2) {
                    PopupCatalog.invalidNumber();
                }
            }
        });
        changeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product product = (Product) comboBoxModifyProduct.getSelectedItem();
                    String name = textFieldChangeProductName.getText();
                    String description = textFieldChangeProductDesc.getText();
                    String imgUrl = textFieldChangeProductImg.getText();
                    int stock = Integer.parseInt(textFieldChangeProductStock.getText());
                    double price;
                    if (textFieldChangeProductPrice.getText().equals("")) {
                        price = -1;
                    } else {
                        price = Double.parseDouble(textFieldChangeProductPrice.getText());
                    }

                    TableProducts.changeProduct(product.getId() , name , price , product.getStock() + stock , 0 , description , imgUrl);

                    //TODO change on focus gain to modify products
                    ComboBoxProductLogic.updateElements();
                    ComboBoxProductLogic.setElements(comboBoxModifyProduct);
                    //ComboBoxProductLogic.setElements(comboBoxProduct);
                } catch (NumberFormatException e1) {
                    PopupCatalog.invalidDouble();
                } catch (WrapperException e2) {
                    PopupCatalog.customError(e2.getWrapperMessage());
                }
            }
        });
        //TODO ok, this fix is so bandage, I want to die just looking at it
        comboBoxModifyProduct.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                try {
                    Product product = (Product) comboBoxModifyProduct.getSelectedItem();
                    labelAddStock.setText("Current stock: " + product.getStock());
                } catch (NullPointerException ignored) {
                }
            }
        });
        changeClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = (Client) comboBoxChangeClient.getSelectedItem();

                    String name = textFieldChangeClientName.getText();
                    String surname = textFieldChangeClientSurname.getText();
                    String address = textFieldChangeClientAddress.getText();
                    String country = textFieldChangeClientCountry.getText();
                    String city = textFieldChangeClientCity.getText();

                    int postalCode;
                    if(textFieldChangeClientPostalCode.getText().equals("")){
                        postalCode = -1;
                    } else {
                        postalCode = Integer.parseInt(textFieldChangeClientPostalCode.getText());
                    }

                    TableClients.changeClient(client.getId(), name, surname, address, country, city, postalCode);

                    //ComboBoxClientLogic.updateComboBox(comboBoxClient);
                    ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                } catch (NumberFormatException e2) {
                    PopupCatalog.invalidNumber();
                }
            }
        });
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = textFieldAddUserUsername.getText();
                    if(username.equals("")) {
                        throw new WrapperException(new EmptyTextFieldException(), "Please enter an username!");
                    }

                    String password = textFieldAddUserPassword.getText();
                    if(!VerificationSyntax.verifyPassword(password)) {
                        throw new WrapperException(new InvalidPasswordException(), "Password must contain minimum" +
                                " eight characters, at least one letter and one number");
                    }

                    String email = textFieldAddUserEmail.getText();
                    if(!VerificationSyntax.verifyEmail(email)) {
                        throw new WrapperException(new InvalidEmailException(), "Please enter a valid email!");
                    }

                    int type = comboBoxAddUserType.getSelectedIndex() + 1; //TODO make this more elegant

                    TableUsers.insertUser(username, password, email, type);
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }
            }
        });
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (PopupCatalog.yesNoDeleteUser("product")) {
                        Product product = (Product) comboBoxModifyProduct.getSelectedItem();
                        TableProducts.deleteProduct(product.getId());

                        ComboBoxProductLogic.updateElements();
                        ComboBoxProductLogic.setElements(comboBoxModifyProduct);
                    }
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }
            }
        });
        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(PopupCatalog.yesNoDeleteUser("client")) {
                        Client client = (Client) comboBoxChangeClient.getSelectedItem();
                        TableClients.deleteClient(client.getId());

                        ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
                    }
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }
            }
        });
        allUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableUsers.setModel(new TableUserModel());
                TableUserModel.setHeaders(jTableUsers);
            }
        });
        userByUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTableUsers.setModel(new TableUserModel(textFieldUserByUsername.getText()));
                TableUserModel.setHeaders(jTableUsers);
            }
        });
        changeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    User user = (User) comboBoxChangeUser.getSelectedItem();

                    String username = textFieldChangeUserUsername.getText();

                    String password = textFieldChangeUserPassword.getText();
                    if(!VerificationSyntax.verifyPassword(password) && !password.equals("")) {
                        throw new WrapperException(new InvalidPasswordException(), "Password must contain minimum" +
                                " eight characters, at least one letter and one number");
                    }

                    String email = textFieldChangeUserEmail.getText();
                    if(!VerificationSyntax.verifyEmail(email) && !email.equals("")) {
                        throw new WrapperException(new InvalidEmailException(), "Please enter a valid email!");
                    }

                    int type = comboBoxAddUserType.getSelectedIndex() + 1; //TODO make this more elegant

                    TableUsers.changeUser(user.getId(), username, email, password, type);

                    ComboBoxUserLogic.updateElements(comboBoxChangeUser);

                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                }
            }
        });
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

    public void createUIComponents() {
        try {
            jTableClients.setModel(new TableClientsModel());
            TableClientsModel.setHeaders(jTableClients);

            jTableProducts.setModel(new TableProductsModel());
            TableProductsModel.setHeaders(jTableProducts);

            jTableSaleHistory.setModel(new TableSalesModel());
            TableSalesModel.setHeaders(jTableSaleHistory);

            jTableUsers.setModel(new TableUserModel());
            TableUserModel.setHeaders(jTableUsers);

            //ComboBoxClientLogic.updateComboBox(comboBoxClient);
            ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
            ComboBoxProductLogic.updateElements();
            //ComboBoxProductLogic.setElements(comboBoxProduct);
            ComboBoxProductLogic.setElements(comboBoxModifyProduct);
            ComboBoxUserTypeLogic.updateElements(comboBoxAddUserType);
            ComboBoxUserTypeLogic.updateElements(comboBoxChangeUserType);
            ComboBoxUserLogic.updateElements(comboBoxChangeUser);
        } catch (WrapperException e1) {
            PopupCatalog.customError(e1.getWrapperMessage());
        }
    }
}
