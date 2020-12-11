package com.company.exceptions;

public class NotEnoughStockException extends Exception{
    @Override
    public String getMessage(){
        return "Not enough stock to make sale!";
    }
}
