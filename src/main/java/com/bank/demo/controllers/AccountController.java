package com.bank.demo.controllers;

import com.bank.demo.data.Account;
import com.bank.demo.dto.CreateRequest;
import com.bank.demo.dto.CustomResponse;
import com.bank.demo.dto.CustomResponseWithData;
import com.bank.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/account_info/{account_number}")
    public ResponseEntity<?> getAccountNumber(@PathVariable String account_number){
        try {
            Account account = accountService.getAccount(account_number);
            return new ResponseEntity<>(new CustomResponseWithData<Account>(200, true, "successful", account), HttpStatus.valueOf(200));
        }catch (IOException ex){
            return new ResponseEntity<>(new CustomResponse(500, false, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/account_statement/{account_number}")
    public String getAccountStatement(@PathVariable String account_number){
        return "hello world";
    }
    @PostMapping("/create_account")
    public ResponseEntity<CustomResponse> create(@Valid @RequestBody CreateRequest request){
        boolean result = accountService.createAccount(request);
        return new ResponseEntity<>(new CustomResponse(200,result,"successfully created"), HttpStatus.OK);
    }
    @PostMapping("/login")
    public String login(){
        return "hello world";
    }
}
