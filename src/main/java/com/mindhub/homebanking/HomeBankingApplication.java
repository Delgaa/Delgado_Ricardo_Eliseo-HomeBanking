package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return args -> {

			Client melba = new Client("Melba","Morel","melba@mindhub.com");
			clientRepository.save(melba);
			System.out.println(melba);

			Client clientYo = new Client("Ricardo", "Delgado", "delgadoricardoeliseo@gmail.com");
			clientRepository.save(clientYo);
			System.out.println(clientYo);
		};
	}
}
