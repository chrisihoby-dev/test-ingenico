package com.example.test.service.mapper;

import com.example.test.model.BankAccount;
import com.example.test.service.dto.BankAccountDto;
import io.vavr.collection.List;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountDto toBankAccountDto(BankAccount bankAccount){
        return BankAccountDto.builder()
                .reference(bankAccount.getReference())
                .balance(bankAccount.getBalance())
                .currency(bankAccount.getCurrency())
                .transactionHistory(bankAccount.getTransactionHistory().asJava())
                .build();
    }

    public BankAccount toBankAccount(BankAccountDto bankAccountDto){
       return  BankAccount.builder()
                .currency(bankAccountDto.getCurrency())
                .balance(bankAccountDto.getBalance())
                .transactionHistory(List.of())
                .reference(bankAccountDto.getReference())
                .build();
    }
}
