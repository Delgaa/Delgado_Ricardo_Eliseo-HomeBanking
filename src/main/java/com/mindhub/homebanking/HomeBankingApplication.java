package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return args -> {

			Client melba = new Client("Melba","Morel","melba@mindhub.com");
			clientRepository.save(melba);
			Account accountMelba1 = new Account("VIN001", LocalDate.now(), 5000.00);
			melba.addAccount(accountMelba1);
			accountRepository.save(accountMelba1);
			Account accountMelba2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.00);
			melba.addAccount(accountMelba2);
			accountRepository.save(accountMelba2);


			System.out.println(melba);

			Client clientYo = new Client("Ricardo", "Delgado", "delgadoricardoeliseo@gmail.com");
			clientRepository.save(clientYo);
			Account accountYo = new Account("VIN003", LocalDate.now(), 7000.00);
			clientYo.addAccount(accountYo);
			accountRepository.save(accountYo);
			Account accountYo2  = new Account("VIN004", LocalDate.now().plusDays(1), 500.00);
			clientYo.addAccount(accountYo2);
			accountRepository.save(accountYo2);


			System.out.println(clientYo);
		};
	}
}
