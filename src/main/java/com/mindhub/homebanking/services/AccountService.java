package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

public interface AccountService {

    Account getAccountById(Long id);
    Account getAccountByNumber(String number);

    void saveAccount(Account account);

    Boolean accountExistsByNumberAndClient(String number, Client client);
}
