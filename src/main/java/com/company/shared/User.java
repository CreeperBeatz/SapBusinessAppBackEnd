package com.company.shared;

import com.company.persistence.TableUsers;

public class User {

    private String username;
    private String password_hash;
    private String email;
    private int type;
    private int id;


    public User() {
    }

    public User( int id, String username, String password_hash,  String email , int type) {
        this.password_hash = password_hash;
        this.username = username;
        this.email = email;
        this.type = type;
        this.id = id;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString(){
        switch (this.type) {
            case TableUsers.INDEX_ADMIN:
                return this.getUsername() + " | Administrator";
            case TableUsers.INDEX_SALESMAN:
                return this.getUsername() + " | Salesman";
            default:
                return this.getUsername() + " | Unknown";
        }
    }
}
