package com.mindhub.homebanking.dtos;

public record LoanApplicationDTO(String name, Double amount, Integer payments, String numberAccount) {
}
