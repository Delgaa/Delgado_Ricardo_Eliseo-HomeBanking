package com.mindhub.homebanking.services.implementsServices;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> getLoansDTO() {
        return getAllLoans().stream().map(LoanDTO::new).toList();
    }

    @Override
    public Loan getLoanByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public Boolean isLoanAvailable(String name) {
        return loanRepository.existsLoanByName(name);
    }

    @Override
    public Boolean isLoanAvailableByNameAndPayment(String name, Integer payments) {
        return loanRepository.existsLoanByNameAndPayments(name, payments);
    }
}
