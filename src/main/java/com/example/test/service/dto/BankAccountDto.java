package com.example.test.service.dto;

import com.example.test.model.Currency;
import com.example.test.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class BankAccountDto {
    private final String reference;
    private final Double balance;
    private final Currency currency;
    private List<Transaction> transactionHistory;
}
