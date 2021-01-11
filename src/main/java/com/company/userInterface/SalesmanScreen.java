package com.company.userInterface;

import com.company.exceptions.EmptyTextFieldException;
import com.company.exceptions.WrapperException;
import com.company.persistence.Datasource;
import com.company.persistence.SqlClients;
import com.company.persistence.SqlProducts;
import com.company.persistence.SqlSales;
import com.company.shared.Client;
import com.company.shared.Product;
import com.company.userInterface.comboBoxLogic.ComboBoxClientLogic;
import com.company.userInterface.comboBoxLogic.ComboBoxProductLogic;
import com.company.utilities.TimeConverter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalesmanScreen extends Thread {
    private final int id;
    private final String username;
    private final String email;


    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel clientsPanel;
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
    private JComboBox comboBoxClient;
    private JComboBox comboBoxProduct;
    private JTextField textFieldSaleQuantity;
    private JButton addSaleButton;
    private JTextField textFieldSaleDiscount;
    private JTextField fromSalesTextField;
    private JTextField toSalesTextField;
    private JButton saleByDateButton;
    private JTable jTableSaleHistory;
    private JScrollPane JScrollPaneClients;
    private JPanel saleHistoryPanel;
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
    private JPanel productsPanel;
    private JPanel addSalePanel;
    private JPanel addClientPanel;
    private JPanel addProduct;
    private JPanel modifyProduct;
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


    /**
     * Sets up the salesman page and keeps it active
     */
    public void run() {
        JFrame salesmanScreen = new JFrame("Welcome, " + username);
        salesmanScreen.setContentPane(this.panel1);
        salesmanScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        salesmanScreen.addWindowListener(exitAppWindowAdapter());
        salesmanScreen.pack();
        salesmanScreen.setVisible(true);

        salesmanScreen.setLocationRelativeTo(null);
        createUIComponents();


    }

    public SalesmanScreen(final int id , final String username ,final String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        clientsByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        addSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantity = Integer.parseInt(textFieldSaleQuantity.getText());
                    if (quantity < 1) {
                        textFieldSaleQuantity.setText("");
                        throw new WrapperException(new NumberFormatException() , "Quantity can't be 0 or lower!");
                    }

                    int discount = Integer.parseInt(textFieldSaleDiscount.getText());
                    if (discount < 0 || discount > 100) {
                        textFieldSaleDiscount.setText("0");
                        throw new WrapperException(new NumberFormatException() , "Discount can't be lower " +
                                "than 0 or higher than 100!");
                    }

                    Client client = (Client) comboBoxClient.getSelectedItem();
                    Product product = (Product) comboBoxProduct.getSelectedItem();

                    SqlSales.insertSale(id , client.getId() , product.getId() , quantity , discount);

                    PopupCatalog.successfulSale();

                    ComboBoxProductLogic.updateElements(); //TODO make this automatic
                    ComboBoxProductLogic.setElements(comboBoxProduct);
                    ComboBoxProductLogic.setElements(comboBoxModifyProduct);
                    ComboBoxClientLogic.updateComboBox(comboBoxClient);
                    ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                } catch (NullPointerException e2) {
                    PopupCatalog.customError("Critical error. Please contact System Admin");
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
                        SqlClients.insertClient(name , surname , address , country , city , postalCode);
                        ComboBoxClientLogic.updateComboBox(comboBoxClient);//TODO automate this
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

                    SqlProducts.insertProduct(name , price , stock , discount , description , imgUrl);

                    ComboBoxProductLogic.updateElements(); //TODO make this automatic
                    ComboBoxProductLogic.setElements(comboBoxProduct);
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

                    SqlProducts.changeProduct(product.getId() , name , price , product.getStock() + stock , 0 , description , imgUrl);

                    //TODO change on focus gain to modify products
                    ComboBoxProductLogic.updateElements();
                    ComboBoxProductLogic.setElements(comboBoxModifyProduct);
                    ComboBoxProductLogic.setElements(comboBoxProduct);
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
        comboBoxProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product product = (Product) comboBoxProduct.getSelectedItem();
                    labelAddSaleCurrentStock.setText("Current stock: " + product.getStock());
                } catch (NullPointerException ignored) {
                }
            }
        });
        comboBoxClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = (Client) comboBoxClient.getSelectedItem();
                    labelTotalPurchases.setText("Total purchases: " + client.getNumOfPurchases());
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

                    SqlClients.changeClient(client.getId(), name, surname, address, country, city, postalCode);

                    ComboBoxClientLogic.updateComboBox(comboBoxClient);
                    ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
                } catch (WrapperException e1) {
                    PopupCatalog.customError(e1.getWrapperMessage());
                } catch (NumberFormatException e2) {
                    PopupCatalog.invalidNumber();
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

            ComboBoxClientLogic.updateComboBox(comboBoxClient);
            ComboBoxClientLogic.updateComboBox(comboBoxChangeClient);
            ComboBoxProductLogic.updateElements();
            ComboBoxProductLogic.setElements(comboBoxProduct);
            ComboBoxProductLogic.setElements(comboBoxModifyProduct);
        } catch (WrapperException e1) {
            PopupCatalog.customError(e1.getWrapperMessage());
        }
    }
}
