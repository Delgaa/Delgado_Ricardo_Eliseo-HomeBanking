package com.mindhub.homebanking.dtos;

public record TransactionApplicationDTO(Double amount, String detail, String numberOrigin, String numberDestination) {
}
