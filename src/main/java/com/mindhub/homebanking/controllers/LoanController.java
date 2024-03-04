package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<Loan> loans = loanRepository.findAll();

        return new ResponseEntity<>(loans.stream().map(LoanDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);


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

        if (!loanRepository.existsLoanByName(loanApplicationDTO.name())){
            return new ResponseEntity<>("Loan already exists", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.amount() > loanRepository.findByName(loanApplicationDTO.name()).getMaxAmount()){
            return new ResponseEntity<>("Amount is greater than max amount", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsAccountByNumberAndClient(loanApplicationDTO.numberAccount(), client)){
            return new ResponseEntity<>("Number account already exists", HttpStatus.FORBIDDEN);
        }

        if (!loanRepository.existsLoanByNameAndPayments(loanApplicationDTO.name(), loanApplicationDTO.payments())){
            return new ResponseEntity<>("Loan already exists", HttpStatus.FORBIDDEN);
        }

        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.amount() + loanApplicationDTO.amount()* 0.2, loanApplicationDTO.payments());

        Loan newLoan = loanRepository.findByName(loanApplicationDTO.name());
        newLoan.addClientLoan(newClientLoan);
        client.addClientLoan(newClientLoan);

        Transaction newTransaction = new Transaction(CREDIT,  loanApplicationDTO.name() +" loan approved", LocalDateTime.now(), loanApplicationDTO.amount());
        Account account = accountRepository.findByNumber(loanApplicationDTO.numberAccount());

        account.addTransaction(newTransaction);
        account.setBalance(account.getBalance() + loanApplicationDTO.amount());

        clientLoanRepository.save(newClientLoan);
        accountRepository.save(account);
        loanRepository.save(newLoan);
        clientRepository.save(client);
        transactionRepository.save(newTransaction);

        return new ResponseEntity<>("Created successfully",HttpStatus.CREATED);
    }
}
