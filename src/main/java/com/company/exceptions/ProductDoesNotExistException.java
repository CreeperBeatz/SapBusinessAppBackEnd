package com.company.exceptions;

public class ProductDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "Product not in database!";
    }
}
