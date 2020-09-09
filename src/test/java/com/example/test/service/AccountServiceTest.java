package com.example.test.service;

import com.example.test.exceptions.AccountAlreadyPresentException;
import com.example.test.exceptions.AccountNotFoundException;
import com.example.test.exceptions.ImpossibleTransactionException;
import com.example.test.model.BankAccount;
import com.example.test.model.Currency;
import com.example.test.service.dto.BankAccountDto;
import io.vavr.collection.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceTest {

    private AccountService accountService;
    @Autowired
    public AccountServiceTest( AccountService accountService){ this.accountService = accountService;}

    @Test
    public void accountCreationAndCreationFailureTest(){
        BankAccountDto newAccount = BankAccountDto.builder()
                .reference("ACCOUNT_USER0")
                .balance(30.0)
                .currency(Currency.EURO)
                .transactionHistory(java.util.List.of())
                .build();

        this.accountService.createAccount(newAccount);

        Assertions.assertTrue(this.accountService.getAccount("ACCOUNT_USER0").getReference().equals("ACCOUNT_USER0"));

        //  AccountAlreadyPresentException should be thrown because user ACCOUNT_USER0 already exists
        AccountAlreadyPresentException exception = assertThrows(AccountAlreadyPresentException.class, () -> {
            this.accountService.createAccount(newAccount);
        });

        Assertions.assertTrue(exception.getMessage().contains("already exists"));

    }

    @Test
    public void depositTest() {
        BankAccount newAccount = BankAccount.builder()
                .reference("ACCOUNT_USER1")
                .balance(0.00).transactionHistory(List.of()).currency(Currency.EURO)
                .build();
        this.accountService.fakeBankAccountRepository.addAccount(newAccount);

        this.accountService.depositAmount(newAccount.getReference(), 10.25);

        var account = this.accountService.getAccount(newAccount.getReference());

        Assertions.assertTrue(account.getBalance().equals(10.25));
        Assertions.assertTrue(account.getTransactionHistory().size()> 0);
    }

    @Test
    public void withdrawalAndNotEnoughAmountTest() {
        BankAccount newAccount = BankAccount.builder()
                .reference("ACCOUNT_USER2")
                .balance(100.00).transactionHistory(List.of()).currency(Currency.DOLLAR)
                .build();
        this.accountService.fakeBankAccountRepository.addAccount(newAccount);

        this.accountService.withdrawAmount(newAccount.getReference(), 10.25);

        var account = this.accountService.getAccount(newAccount.getReference());

        Assertions.assertTrue(account.getBalance().equals(89.75));
        Assertions.assertTrue(account.getTransactionHistory().size()> 0);


        // ImpossibleTransactionException should be  thrown because the amount is higher than the amount available in the account
        ImpossibleTransactionException exception = assertThrows(ImpossibleTransactionException.class, () -> {
            this.accountService.withdrawAmount("ACCOUNT_USER2", 150.00);
        });
        Assertions.assertTrue(exception.getMessage().contains("not enough amount in your account"));



    }
    @Test
    public void nonExistingUserAccountTest(){
        // AccountNotFoundException should be thrown when user doesn't exist

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            this.accountService.getAccount("NON_EXISTING_USER");
        });

        Assertions.assertTrue(exception.getMessage().contains("NON_EXISTING_USER"));
    }

}
