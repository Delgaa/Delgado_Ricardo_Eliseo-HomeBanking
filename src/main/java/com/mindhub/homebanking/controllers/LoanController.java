package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanVerificationService loanVerificationService;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<Loan> loans = loanService.getAllLoans();
        return new ResponseEntity<>(loanService.getLoansDTO(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO){
        return loanVerificationService.verifyLoanApplication(loanApplicationDTO);
    }
}
