package com.example.test.service;

import com.example.test.exceptions.AccountAlreadyPresentException;
import com.example.test.exceptions.AccountNotFoundException;
import com.example.test.exceptions.ImpossibleTransactionException;
import com.example.test.model.BankAccount;
import com.example.test.model.Transaction;
import com.example.test.model.TransactionType;
import com.example.test.repository.FakeBankAccountRepository;
import com.example.test.service.dto.BankAccountDto;
import com.example.test.service.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;


@Service
public class AccountService {
    public FakeBankAccountRepository fakeBankAccountRepository;
    public BankAccountMapper bankAccountMapper;
    @Autowired
    public AccountService(FakeBankAccountRepository fakeBankAccountRepository, BankAccountMapper bankAccountMapper){
        this.fakeBankAccountRepository = fakeBankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    public void createAccount(BankAccountDto bankAccountDto){
        if(this.fakeBankAccountRepository.isAcoountPresent(bankAccountDto.getReference())){
            throw new AccountAlreadyPresentException("accountref "+bankAccountDto.getReference()+" already exists");
        }
        this.fakeBankAccountRepository.addAccount(this.bankAccountMapper.toBankAccount(bankAccountDto));
    }

    public BankAccountDto getAccount(String accountRef) throws AccountNotFoundException{
        return this.fakeBankAccountRepository.getAccount(accountRef).map(this.bankAccountMapper::toBankAccountDto)
                .getOrElseThrow(()-> new AccountNotFoundException("account ref :"+accountRef+ "doesn't exist"));
    }

    public void withdrawAmount(String accountRef, Double amount){
        BankAccount account = this.fakeBankAccountRepository.getAccount(accountRef).map(acc -> this.removeFromAccount(acc,amount))
                .getOrElseThrow(()-> new AccountNotFoundException("account ref :"+accountRef+ "doesn't exist"));
        this.fakeBankAccountRepository.updateAccount(account);
    }

    public void depositAmount(String accountRef, Double amount){
        BankAccount account = this.fakeBankAccountRepository.getAccount(accountRef).map(acc ->  this.addToAccount(acc,amount) )
                .getOrElseThrow(()-> new AccountNotFoundException("account ref :"+accountRef+ "doesn't exist"));;
        this.fakeBankAccountRepository.updateAccount(account);
    }

    public BankAccount addToAccount(BankAccount account,Double amount){
        var transactions = account.getTransactionHistory().append(Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(amount).date(ZonedDateTime.now()).build());
        return account.toBuilder()
                .balance(account.getBalance() + amount)
                .transactionHistory(transactions).build();
    }
    public BankAccount removeFromAccount(BankAccount account,Double amount) throws ImpossibleTransactionException{
        var transactions = account.getTransactionHistory().append(Transaction.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(amount).date(ZonedDateTime.now()).build());
        var diff = account.getBalance() - amount;
        if(diff < 0.0){
            throw new ImpossibleTransactionException("not enough amount in your account");
        }
        return account.toBuilder().balance(diff).transactionHistory(transactions).build();
    }

    public List<BankAccountDto> getAll(){
        return this.fakeBankAccountRepository.getAll().map(this.bankAccountMapper::toBankAccountDto).asJava();
    }
}
