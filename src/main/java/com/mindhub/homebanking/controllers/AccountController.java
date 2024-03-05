package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients/current/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAccounts(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        return new ResponseEntity<>(accountService.getAccountsDTO(client), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id){
        Account account = accountService.getAccountById(id);
        return account != null ? new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK): ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    @PostMapping("/")
    public ResponseEntity<?> addAccount(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        if (client.getAccounts().size() == 3){
            return new ResponseEntity<>("The maximum of three accounts has already been reached.",HttpStatus.FORBIDDEN);
        }

        GenerateRandomNum generateRandomNumAccount = new GenerateRandomNum();
        Account newAccount = new Account("VIN-" + generateRandomNumAccount.getRandomNumber(1,1000000), LocalDate.now(), 0.0);
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        clientService.saveClient(client);

        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }
}
