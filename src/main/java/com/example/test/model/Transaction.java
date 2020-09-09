package com.example.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Transaction {
    private Double amount;
    private ZonedDateTime date;
    private TransactionType transactionType;


}
