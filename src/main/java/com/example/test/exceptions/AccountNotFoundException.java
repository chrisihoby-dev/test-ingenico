package com.example.test.exceptions;

import lombok.Getter;

@Getter
public class AccountNotFoundException  extends RuntimeException{
    private String message;

    public AccountNotFoundException(String message){
        this.message = message;
    }
}
