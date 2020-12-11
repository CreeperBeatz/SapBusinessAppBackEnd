package com.company.exceptions;

public class ClientDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "Client not in database!";
    }
}
