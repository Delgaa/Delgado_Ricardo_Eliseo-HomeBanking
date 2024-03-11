package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {


    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testExistsLoan(){
        Boolean existsLoan = loanRepository.existsLoanByName("Mortgage");
        assertThat(existsLoan, is(true));
    }
    @Test
    public void testExistsPaymentsByLoan() {
        Boolean existsWithPayments = loanRepository.existsLoanByNameAndPayments("Mortgage", 12);
        assertThat(existsWithPayments, is(true));
    }

    @Test
    public void testAccount(){
        Account foundAccount = accountRepository.findByNumber("VIN001");
        assertThat(foundAccount, not(nullValue()));
    }

    @Test
    public void testAccountExists(){
        Client testClient = clientRepository.findByEmail("melba@mindhub.com");
        Boolean existsWithClient = accountRepository.existsAccountByNumberAndClient("VIN001", testClient);
        assertThat(existsWithClient, is(true));
    }

    @Test
    public void testClientExists(){
        Boolean existsClient = clientRepository.existsClientByEmail("melba@mindhub.com");
        assertThat(existsClient, is(true));
    }

    @Test
    public void testFindDataClient(){
        Client foundClient = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(foundClient.getName(), is("Melba"));
        assertThat(foundClient.getLastName(), is("Morel"));
    }

    @Test
    public void testCardExists(){
        Client testClient = clientRepository.findByEmail("delgadoricardoeliseo@gmail.com");
        Boolean exists = cardRepository.existsCardByTypeAndColorAndClient(CardType.CREDIT, CardColor.SILVER, testClient);
        assertThat(exists, is(true));

    }

    @Test
    public void testCardByCardHolder(){
        List<Card> cards = cardRepository.findByCardHolder("Melba Morel");
        assertThat(cards.size(), is(2));
        assertThat(cards.get(0).getType(), is(CardType.DEBIT));
        assertThat(cards.get(1).getColor(), is(CardColor.TITANIUM));
    }

    @Test
    public void testClientLoanExists(){
        Client testClient = clientRepository.findByEmail("melba@mindhub.com");
        Loan testLoan = loanRepository.findByName("Mortgage");
        Boolean exists = clientLoanRepository.existsLoanByLoanAndClient(testLoan, testClient);
        assertThat(exists, is(true));
    }
    @Test
    public void testClientLoan() {
        Client testClient = clientRepository.findByEmail("melba@mindhub.com");
        List<ClientLoan> clientLoans = clientLoanRepository.findByClient(testClient);
        assertThat(clientLoans.size(), is(2));
        assertThat(clientLoans.get(0).getLoan().getName(), is("Mortgage"));
    }

    @Test
    public void testTransaction(){
        Optional<Transaction> testTransaction = transactionRepository.findById(2L);
        assertThat(testTransaction, not(nullValue()));
    }


    @Test
    public void testTransactionExists(){
        Account testAccount = accountRepository.findByNumber("VIN001");
        Boolean exists = transactionRepository.existsTransactionByAccount(testAccount);
        assertThat(exists, is(true));
    }
}
