package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionApplicationDTO;
import com.mindhub.homebanking.services.TransactionVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionVerificationService transactionVerificationService;

    @PostMapping("/")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionApplicationDTO transactionApplicationDTO){
       return transactionVerificationService.verifyTransactionApplication(transactionApplicationDTO);
    }


}