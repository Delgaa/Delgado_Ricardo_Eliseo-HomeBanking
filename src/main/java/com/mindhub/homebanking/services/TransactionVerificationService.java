package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class TransactionVerificationService {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Transactional
    public ResponseEntity<?> verifyTransactionApplication(TransactionApplicationDTO transactionApplicationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        if (transactionApplicationDTO.numberOrigin().isBlank()){
            return new ResponseEntity<>("Number account origin is empty", HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.numberDestination().isBlank()){
            return new ResponseEntity<>("Number account destination is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.amount() == null){
            return new ResponseEntity<>("Amount is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.detail().isBlank()){
            return new ResponseEntity<>("Description is empty",HttpStatus.FORBIDDEN);
        }

        if (transactionApplicationDTO.numberOrigin().equals(transactionApplicationDTO.numberDestination())){
            return new ResponseEntity<>("Account origin is equals account destination",HttpStatus.FORBIDDEN);
        }

        if (!accountService.accountExistsByNumber(transactionApplicationDTO.numberOrigin())){
            return new ResponseEntity<>("The account origin not exist", HttpStatus.FORBIDDEN);
        }

        if (!accountService.accountExistsByNumberAndClient(transactionApplicationDTO.numberOrigin(), client)){
            return new ResponseEntity<>("The account origin not is your account", HttpStatus.FORBIDDEN);
        }

        if (!accountService.accountExistsByNumber(transactionApplicationDTO.numberDestination())){
            return new ResponseEntity<>("The account destination not exist", HttpStatus.FORBIDDEN);
        }

        Account accountOrigin = accountService.getAccountByNumber(transactionApplicationDTO.numberOrigin());

        if(transactionApplicationDTO.amount() > accountOrigin.getBalance()){
            return new ResponseEntity<>("The available balance is insufficient", HttpStatus.FORBIDDEN);
        }

        Transaction DebitTransaction = new Transaction(TransactionType.DEBIT,
                transactionApplicationDTO.detail() + " to " + transactionApplicationDTO.numberDestination(),
                LocalDateTime.now(),
                -transactionApplicationDTO.amount());

        accountOrigin.setBalance((accountOrigin.getBalance() - transactionApplicationDTO.amount()));
        accountOrigin.addTransaction(DebitTransaction);
        transactionService.saveTransaction(DebitTransaction);
        accountService.saveAccount(accountOrigin);

        Account accountDestination = accountService.getAccountByNumber(transactionApplicationDTO.numberDestination());

        Transaction CreditTransaction = new Transaction(TransactionType.CREDIT,
                transactionApplicationDTO.detail() +" of "+ transactionApplicationDTO.numberOrigin(),
                LocalDateTime.now(),
                transactionApplicationDTO.amount());

        accountDestination.setBalance((accountDestination.getBalance() + transactionApplicationDTO.amount()));
        accountDestination.addTransaction(CreditTransaction);
        transactionService.saveTransaction(CreditTransaction);
        accountService.saveAccount(accountDestination );

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }
}
