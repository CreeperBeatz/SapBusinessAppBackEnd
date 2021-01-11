package com.company.userInterface;

import com.company.exceptions.WrapperException;
import com.company.persistence.SqlUsers;
import com.company.shared.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableUserModel extends AbstractTableModel {

    private static String[] columnNames = {SqlUsers.COLUMN_USERS_ID , SqlUsers.COLUMN_USERS_USERNAME ,
            SqlUsers.COLUMN_USERS_EMAIL, SqlUsers.COLUMN_USERS_TYPE};

    private Object[][] data;

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex , int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    //All users
    public TableUserModel() {
        data = getAllUsers();
    }

    //search by username
    public TableUserModel(String username) {
        data = getUsersByUsername(username);
    }

    private Object[][] getAllUsers(){
        List<User> userList = SqlUsers.queryAllUsers();
        return getDataFromUsersList(userList);
    }

    private Object[][] getUsersByUsername(String username) {
        try {
            List<User> userList = SqlUsers.queryUsersByUsername(username);
            return getDataFromUsersList(userList);
        } catch (WrapperException e) {
            PopupCatalog.customError(e.getWrapperMessage());
            return null;
        }
    }

    private Object[][] getDataFromUsersList(List<User> queryData) {
        Object[][] users;

        if(queryData.size() == 0) {
            users = new Object[1][columnNames.length];
            for(int i = 0; i < columnNames.length; i++) {
                users[0][i] = "";
            }
            return users;
        }

        users = new Object[queryData.size()][columnNames.length];

        int j = 0;
        for(User current: queryData) {
            users[j][0] = current.getId();
            users[j][1] = current.getUsername();
            users[j][2] = current.getEmail();

            switch (current.getType()) {
                case SqlUsers.INDEX_ADMIN:
                    users[j][3] =  "Administrator";
                    break;
                case SqlUsers.INDEX_SALESMAN:
                    users[j][3] =  "Salesman";
                    break;
                default:
                    users[j][3] =  "Unknown";
                    break;
            }
            j++;
        }

        return users;
    }

    public static void setHeaders(JTable table) {
        for(int i = 0; i< columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
        }
        table.getColumnModel().getColumn(0).setMaxWidth(30);
    }
}
