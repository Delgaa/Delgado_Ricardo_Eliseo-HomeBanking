package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;


public class ClientDTO {
    private Long id;
    private String firstName, lastNAme, email;
    private Set<AccountDTO> accounts;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getName();
        this.lastNAme = client.getLastName();
        this.email = client.getEmail();
        this.accounts =client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastNAme() {
        return lastNAme;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
