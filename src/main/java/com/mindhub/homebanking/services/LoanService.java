package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> getAllLoans();

    List<LoanDTO> getLoansDTO();

    Loan getLoanByName(String name);

    Boolean isLoanAvailable(String name);

    Boolean isLoanAvailableByNameAndPayment(String name, Integer payments);
}
