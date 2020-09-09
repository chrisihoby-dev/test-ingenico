package com.example.test.exceptions;

import lombok.Getter;

@Getter
public class AccountAlreadyPresentException extends RuntimeException{
    private String message;

    public AccountAlreadyPresentException(String message){
        this.message = message;
    }
}
