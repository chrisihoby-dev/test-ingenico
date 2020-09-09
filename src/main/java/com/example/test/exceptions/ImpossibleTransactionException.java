package com.example.test.exceptions;

import lombok.Getter;

@Getter
public class ImpossibleTransactionException extends RuntimeException{
    private String message;

    public ImpossibleTransactionException(String message){
        this.message = message;
    }
}
