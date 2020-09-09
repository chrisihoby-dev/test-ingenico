package com.example.test.rest;

import com.example.test.exceptions.AccountAlreadyPresentException;
import com.example.test.exceptions.AccountNotFoundException;
import com.example.test.exceptions.ImpossibleTransactionException;
import com.example.test.service.AccountService;
import com.example.test.service.dto.BankAccountDto;
import com.example.test.service.dto.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private AccountService accountService;

    public BankAccountController(AccountService accountService){this.accountService=accountService;}
    @GetMapping("/{accountRef}")
    public ResponseEntity<BankAccountDto> getAccount(@PathVariable String accountRef){
        try{
            return ResponseEntity.ok(this.accountService.getAccount(accountRef));
        }catch(AccountNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankAccountDto>> getAll(){
        return ResponseEntity.ok(this.accountService.getAll());
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity createAccount(@RequestBody BankAccountDto bankAccountDto){
        try{
            this.accountService.createAccount(bankAccountDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully registered");
        }catch (AccountAlreadyPresentException accountAlreadyPresentException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, accountAlreadyPresentException.getMessage());
        }
    }
    @PutMapping("/withdrawal/{accountRef}")
    public ResponseEntity withDrawAmount(@PathVariable String accountRef, @RequestBody TransactionDto transactionDto){
        try{
            this.accountService.withdrawAmount(accountRef,transactionDto.getAmount());
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction successfully applied");
        }catch (AccountNotFoundException accountNotFoundException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, accountNotFoundException.getMessage());
        }catch (ImpossibleTransactionException impossibleTransactionException){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, impossibleTransactionException.getMessage());
        }
    }

    @PutMapping("/deposit/{accountRef}")
    public ResponseEntity depositAmount(@PathVariable String accountRef, @RequestBody TransactionDto transactionDto){
        try{
            this.accountService.depositAmount(accountRef,transactionDto.getAmount());
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction successfully applied");
        }catch (AccountNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }catch (ImpossibleTransactionException exception){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage());
        }
    }

}
