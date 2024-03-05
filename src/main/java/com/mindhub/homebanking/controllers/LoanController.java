package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<Loan> loans = loanService.getAllLoans();

        return new ResponseEntity<>(loanService.getLoansDTO(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);


        if (loanApplicationDTO.name().isBlank()){
            return new ResponseEntity<>("Name is empty", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.amount() == null || loanApplicationDTO.amount() <= 0){
            return new ResponseEntity<>("Amount is empty", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.payments() == null || loanApplicationDTO.payments() == 0){
            return new ResponseEntity<>("Payments is empty", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.numberAccount().isBlank()){
            return new ResponseEntity<>("Number account is empty", HttpStatus.FORBIDDEN);
        }

        if (!loanService.isLoanAvailable(loanApplicationDTO.name())){
            return new ResponseEntity<>("Loan already exists", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.amount() > loanService.getLoanByName(loanApplicationDTO.name()).getMaxAmount()){
            return new ResponseEntity<>("Amount is greater than max amount", HttpStatus.FORBIDDEN);
        }

        if (!accountService.accountExistsByNumberAndClient(loanApplicationDTO.numberAccount(), client)){
            return new ResponseEntity<>("Number account already exists", HttpStatus.FORBIDDEN);
        }

        if (!loanService.isLoanAvailableByNameAndPayment(loanApplicationDTO.name(), loanApplicationDTO.payments())){
            return new ResponseEntity<>("Loan already exists", HttpStatus.FORBIDDEN);
        }

        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.amount() + loanApplicationDTO.amount()* 0.2, loanApplicationDTO.payments());

        Loan newLoan = loanService.getLoanByName(loanApplicationDTO.name());
        newLoan.addClientLoan(newClientLoan);
        client.addClientLoan(newClientLoan);

        Transaction newTransaction = new Transaction(CREDIT,  loanApplicationDTO.name() +" loan approved", LocalDateTime.now(), loanApplicationDTO.amount());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.numberAccount());

        account.addTransaction(newTransaction);
        account.setBalance(account.getBalance() + loanApplicationDTO.amount());

        clientLoanService.saveClientLoan(newClientLoan);
        accountService.saveAccount(account);
        transactionService.saveTransaction(newTransaction);
        clientService.saveClient(client);

        return new ResponseEntity<>("Created successfully",HttpStatus.CREATED);
    }
}
