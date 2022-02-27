package com.bank.demo.service;

import com.bank.demo.data.Account;
import com.bank.demo.dto.CreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StoreService storeService;

    public Account getAccount(String accountNo) throws IOException {
        String data = storeService.readData();
        if(data.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, "Account Files are not Registered");
        }
        Map<String, Map<String, Object>> map = storeService.convertToMap(data);
        Map<String, Object> account = map.get("accounts");
        if(account.containsKey(accountNo)){
            Object acc = account.get(accountNo);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(acc, Account.class);
        }
        throw new ResponseStatusException(NOT_FOUND, "Account not found");
    }
    public boolean createAccount(CreateRequest request){
        request.setAccountPassword(passwordEncoder.encode(request.getAccountPassword()));
        String accountNo = generateRandomNumbers(10);
        Account account = new Account(request.getAccountName(), accountNo, request.getInitialDeposit(), request.getAccountPassword());
        try{
            String data = storeService.readData();
            if(data.isEmpty()){
                Map<String, Map<String, Object>> db = new HashMap<>();
                db.put("accounts", new HashMap<>(){{
                    put(account.getAccountNumber(), account);
                }});
                storeService.writeData(db);
                return true;
            }
            Map<String, Map<String, Object>> map = storeService.convertToMap(data);
            map.get("accounts").put(account.getAccountNumber(), account);
            storeService.writeData(map);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String generateRandomNumbers(int max){
        StringBuilder string = new StringBuilder(max);
        for(int i = 0; i<max; i++){
            Random randNum = new Random();
            int x = randNum.nextInt(9);
            string.append(x);
        }
        return string.toString();
    }
}
