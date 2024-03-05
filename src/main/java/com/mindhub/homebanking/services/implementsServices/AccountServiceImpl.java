package com.mindhub.homebanking.services.implementsServices;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts(Client client) {
        return client.getAccounts().stream().toList();
    }

    @Override
    public List<AccountDTO> getAccountsDTO(Client client) {
        return getAllAccounts(client).stream().map(AccountDTO::new).toList();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Boolean accountExistsByNumber(String number) {
        return accountRepository.existsAccountByNumber(number);
    }

    @Override
    public Boolean accountExistsByNumberAndClient(String number, Client client) {
        return accountRepository.existsAccountByNumberAndClient(number, client);
    }
}
