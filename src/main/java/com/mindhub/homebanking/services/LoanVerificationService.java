package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;

@Service
public class LoanVerificationService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public ResponseEntity<?> verifyLoanApplication(LoanApplicationDTO loanApplicationDTO) {

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

        if (clientLoanService.isLoanAvailableByLoanAndClient(loanService.getLoanByName(loanApplicationDTO.name()), client)){
            return new ResponseEntity<>("Previously obtained loan", HttpStatus.FORBIDDEN);
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
