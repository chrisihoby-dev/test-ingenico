package com.example.test.repository;

import com.example.test.exceptions.AccountNotFoundException;
import com.example.test.model.BankAccount;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;

import io.vavr.collection.List;

@Component
public class FakeBankAccountRepository {
    List<BankAccount> accounts;

    FakeBankAccountRepository(){
        this.accounts = List.of();
    }

    public void addAccount(BankAccount account){
        this.accounts = this.accounts.append(account);
    }

    public Option<BankAccount> getAccount(String accountRef){
        return this.accounts.find(account -> account.getReference().equals(accountRef));

    }

    public List<BankAccount> getAll(){
        return this.accounts;
    }

    public void updateAccount(BankAccount account) throws AccountNotFoundException{
        BankAccount retrievedAndModifiedAccount = this.getAccount(account.getReference()).map(acc -> {
            return acc.toBuilder().balance(account.getBalance())
                    .transactionHistory(account.getTransactionHistory())
                    .build();
        }).getOrElseThrow(()-> new AccountNotFoundException("account ref :"+account.getReference() + "doesn't exist"));
        this.accounts = this.accounts.filter(acc -> !acc.getReference().equals(account.getReference())).append(retrievedAndModifiedAccount);
    }

   public Boolean isAcoountPresent(String accountRef){
        return !this.accounts.find(acc -> acc.getReference().equals(accountRef)).isEmpty();
   }
}
