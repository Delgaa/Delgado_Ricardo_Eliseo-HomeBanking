package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients/current/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllAccounts(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        List<Account> accounts = client.getAccounts().stream().toList();
        return new ResponseEntity<>(accounts.stream().map(AccountDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);
        return account != null ? new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK): ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    @PostMapping("/")
    public ResponseEntity<?> addAccount(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        if (client.getAccounts().size() == 3){
            return new ResponseEntity<>("The maximum of three accounts has already been reached.",HttpStatus.FORBIDDEN);
        }

        GenerateRandomNum generateRandomNumAccount = new GenerateRandomNum();
        Account newAccount = new Account("VIN-" + generateRandomNumAccount.getRandomNumber(1,1000000), LocalDate.now(), 0.0);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);
        clientRepository.save(client);

        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }
}
