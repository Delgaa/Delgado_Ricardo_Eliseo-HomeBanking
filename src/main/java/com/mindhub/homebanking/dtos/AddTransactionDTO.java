package com.mindhub.homebanking.dtos;

public record AddTransactionDTO(Double amount, String detail, String numberOrigin, String numberDestination) {
}
