package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionApplicationDTO transactionApplicationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        if (transactionApplicationDTO.numberOrigin().isBlank()){
            return new ResponseEntity<>("Number account origin is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.numberDestination().isBlank()){
            return new ResponseEntity<>("Number account destination is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.detail().isBlank()){
            return new ResponseEntity<>("Description is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.amount() == null){
            return new ResponseEntity<>("Amount is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.numberOrigin().equals(transactionApplicationDTO.numberDestination())){
            return new ResponseEntity<>("Account origin is equals account destination",HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsAccountByNumber(transactionApplicationDTO.numberOrigin())){
            return new ResponseEntity<>("The account origin not exist", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsAccountByNumberAndClient(transactionApplicationDTO.numberOrigin(), client)){
            return new ResponseEntity<>("The account origin not is your account", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsAccountByNumber(transactionApplicationDTO.numberDestination())){
            return new ResponseEntity<>("The account destination not exist", HttpStatus.FORBIDDEN);
        }

        Account accountOrigin = accountRepository.findByNumber(transactionApplicationDTO.numberOrigin());

        if(transactionApplicationDTO.amount() > accountOrigin.getBalance()){
            return new ResponseEntity<>("The available balance is insufficient", HttpStatus.FORBIDDEN);
        }

        Transaction DebitTransaction = new Transaction(TransactionType.DEBIT,
                transactionApplicationDTO.detail() + " to " + transactionApplicationDTO.numberDestination(),
                LocalDateTime.now(),
                -transactionApplicationDTO.amount());

        accountOrigin.setBalance((accountOrigin.getBalance() - transactionApplicationDTO.amount()));
        accountOrigin.addTransaction(DebitTransaction);
        transactionRepository.save(DebitTransaction);
        accountRepository.save(accountOrigin);

        Account accountDestination = accountRepository.findByNumber(transactionApplicationDTO.numberDestination());

        Transaction CreditTransaction = new Transaction(TransactionType.CREDIT,
                transactionApplicationDTO.detail() +" of "+ transactionApplicationDTO.numberOrigin(),
                LocalDateTime.now(),
                transactionApplicationDTO.amount());
        accountDestination.setBalance((accountDestination.getBalance() + transactionApplicationDTO.amount()));
        accountDestination.addTransaction(CreditTransaction);
        transactionRepository.save(CreditTransaction);
        accountRepository.save(accountDestination );

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }


}