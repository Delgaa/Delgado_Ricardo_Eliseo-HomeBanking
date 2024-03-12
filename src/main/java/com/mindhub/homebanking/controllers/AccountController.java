package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.AccountVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clients/current/accounts")
public class AccountController {

    @Autowired
    private AccountVerificationService accountVerificationService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAccounts(){
        return accountVerificationService.verifyGetAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id){
        return accountVerificationService.verifyGetAccount(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> addAccount(){
        return accountVerificationService.verifyAccountApplication();
    }
}
