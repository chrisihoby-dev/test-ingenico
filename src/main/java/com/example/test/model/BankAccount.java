package com.example.test.model;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class BankAccount {
    private final String reference;
    private final Double balance;
    private final Currency currency;
    private List<Transaction> transactionHistory;


}
