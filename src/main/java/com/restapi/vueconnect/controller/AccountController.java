package com.restapi.vueconnect.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.vueconnect.model.entity.Account;
import com.restapi.vueconnect.model.repository.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @CrossOrigin
    public Account signup(@RequestBody Account account)throws Exception{
        try{
            Optional<Account> searchData = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
            if(searchData.isPresent()) throw new Exception("すでに存在するアカウントです");
            
            String hashPass = passwordEncoder.encode(account.getPassword());
            
            account.setPassword(hashPass);
            account.setRoles("ROLE_GENERAL");
            return accountRepository.save(account);
        }catch(Exception e){
            return account;
        }
    }
}
