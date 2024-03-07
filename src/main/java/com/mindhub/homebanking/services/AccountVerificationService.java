package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AccountVerificationService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    public ResponseEntity<?> verifyGetAllAccounts(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        return new ResponseEntity<>(accountService.getAccountsDTO(client), HttpStatus.OK);
    }

    public ResponseEntity<?> verifyGetAccount(Long id){
        Account account = accountService.getAccountById(id);
        return account != null ? new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK): ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    public ResponseEntity<?> verifyAccountApplication(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        if (client.getAccounts().size() == 3){
            return new ResponseEntity<>("The maximum of three accounts has already been reached.", HttpStatus.FORBIDDEN);
        }

        GenerateRandomNum generateRandomNumAccount = new GenerateRandomNum();

        String numNewAccount;

        do{
            numNewAccount = generateRandomNumAccount.getRandomNumberAccount();
        }while (accountService.accountExistsByNumber(numNewAccount));

        Account newAccount = new Account(numNewAccount, LocalDate.now(), 0.0);
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        clientService.saveClient(client);

        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }
}
