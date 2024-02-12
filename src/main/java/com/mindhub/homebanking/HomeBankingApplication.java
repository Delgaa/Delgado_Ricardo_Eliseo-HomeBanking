package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static com.mindhub.homebanking.models.TransactionType.*;

@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client melba = new Client("Melba","Morel","melba@mindhub.com");
			clientRepository.save(melba);

			Account accountMelba1 = new Account("VIN001", LocalDate.now(), 5000.00);
			melba.addAccount(accountMelba1);
			accountRepository.save(accountMelba1);
			Transaction buyMcDonald = new Transaction(DEBIT, "Buy Grand Tasty Turbo Bacon-McDonald's", LocalDateTime.now(),-3000.00);
			accountMelba1.addTransaction(buyMcDonald);
			transactionRepository.save(buyMcDonald);
			Transaction transfer = new Transaction(CREDIT, "Transfer of Ricardo Delgado", LocalDateTime.now().plusDays(2),1500.00);
			accountMelba1.addTransaction(transfer);
			transactionRepository.save(transfer);

			Account accountMelba2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.00);
			melba.addAccount(accountMelba2);
			accountRepository.save(accountMelba2);
			Transaction buyHeadphones = new Transaction(DEBIT, "Buy Motorola Headphones",LocalDateTime.now().plusDays(5),-5000.0);
			accountMelba2.addTransaction(buyHeadphones);
			transactionRepository.save(buyHeadphones);

			clientRepository.save(melba);
			

			Client clientYo = new Client("Ricardo", "Delgado", "delgadoricardoeliseo@gmail.com");
			clientRepository.save(clientYo);

			Account accountYo = new Account("VIN003", LocalDate.now(), 7000.00);
			clientYo.addAccount(accountYo);
			accountRepository.save(accountYo);
			Transaction buySport = new Transaction(DEBIT, "Buy Nike Court Vision Low", LocalDateTime.now().plusDays(1),-3000.00);
			accountYo.addTransaction(buySport);
			transactionRepository.save(buySport);
			Transaction deposit = new Transaction(CREDIT, "Salary deposit", LocalDateTime.now().plusDays(3),15000.00);
			accountYo.addTransaction(deposit);
			transactionRepository.save(deposit);

			Account accountYo2  = new Account("VIN004", LocalDate.now().plusDays(1), 500.00);
			clientYo.addAccount(accountYo2);
			accountRepository.save(accountYo2);
			Transaction refund = new Transaction(CREDIT, "Refund of the buy of passage",LocalDateTime.now().plusDays(4),5000.0);
			accountYo2.addTransaction(refund);
			transactionRepository.save(refund);

			clientRepository.save(clientYo);

			Loan mortgage = new Loan("Mortgage", 500000, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000, List.of(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000, List.of(6, 12, 24, 36));
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			ClientLoan loan1 = new ClientLoan(400000,60);
			ClientLoan loan2 = new ClientLoan(50000,12);
			ClientLoan loan3 = new ClientLoan(100000,24);
			ClientLoan loan4 = new ClientLoan(200000,36);
			clientLoanRepository.save(loan1);
			clientLoanRepository.save(loan2);
			clientLoanRepository.save(loan3);
			clientLoanRepository.save(loan4);

			melba.addClientLoan(loan1);
			melba.addClientLoan(loan2);
			clientRepository.save(melba);

			clientYo.addClientLoan(loan3);
			clientYo.addClientLoan(loan4);
			clientRepository.save(clientYo);

			mortgage.addClientLoan(loan1);
			personal.addClientLoan(loan2);
			personal.addClientLoan(loan3);
			automotive.addClientLoan(loan4);

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			clientLoanRepository.save(loan1);
			clientLoanRepository.save(loan2);
			clientLoanRepository.save(loan3);
			clientLoanRepository.save(loan4);

			Card cardMelba = new Card(melba, CardType.DEBIT,CardColor.GOLD,"3453-4532-6432-4353",344,LocalDate.now().plusYears(5), LocalDate.now());
			Card cardMelba2 = new Card(melba, CardType.CREDIT,CardColor.TITANIUM,"4532-2326-6242-5632",454,LocalDate.now().plusYears(5), LocalDate.now());
			Card cardClientYo = new Card(clientYo, CardType.CREDIT,CardColor.SILVER,"2344-5412-3643-4633",677,LocalDate.now().plusYears(5), LocalDate.now());
			cardRepository.save(cardMelba);
			cardRepository.save(cardMelba2);
			cardRepository.save(cardClientYo);

			melba.addCards(cardMelba);
			melba.addCards(cardMelba2);
			clientRepository.save(melba);
			clientYo.addCards(cardClientYo);
			clientRepository.save(clientYo);

			cardRepository.save(cardMelba);
			cardRepository.save(cardMelba2);
			cardRepository.save(cardClientYo);

		};
	}
}
