package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<Account>getAllAccounts(Client client);

    List<AccountDTO> getAccountsDTO(Client client);
    Account getAccountById(Long id);
    Account getAccountByNumber(String number);

    void saveAccount(Account account);

    Boolean accountExistsByNumber(String number);

    Boolean accountExistsByNumberAndClient(String number, Client client);

    class LoanService {
    }
}
