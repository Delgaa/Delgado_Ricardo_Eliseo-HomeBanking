package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static com.mindhub.homebanking.models.TransactionType.*;


@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
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


			System.out.println(melba);

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


			System.out.println(clientYo);
		};
	}
}
