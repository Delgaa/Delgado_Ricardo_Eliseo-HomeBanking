package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AddCardDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllCards(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        String cardHolder = client.getName() + " " + client.getLastName();
        List<Card> cards = cardRepository.findByCardHolder(cardHolder);

        return new ResponseEntity<>(cards.stream().map(CardDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/")
    private ResponseEntity<?> addCard(@RequestBody AddCardDTO addCardDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        if (addCardDTO.type().isBlank()){
            return new ResponseEntity<>("Type no content", HttpStatus.BAD_REQUEST);
        }

        if (addCardDTO.color().isBlank()){
            return new ResponseEntity<>("Color no content", HttpStatus.BAD_REQUEST);
        }

        if (cardRepository.existsCardByTypeAndColorAndClient(CardType.valueOf(addCardDTO.type()), CardColor.valueOf(addCardDTO.color()), client)){
            return new ResponseEntity<>("You already have a card of type " + addCardDTO.type().toLowerCase() +" with the color " + addCardDTO.color().toLowerCase(), HttpStatus.FORBIDDEN);
        }

        if (cardRepository.countByTypeAndClient(CardType.valueOf(addCardDTO.type()), client) == 3){
            return new ResponseEntity<>("The maximum number of" +addCardDTO.type().toLowerCase()+ "cards allowed has been reached", HttpStatus.FORBIDDEN);
        }

        GenerateRandomNum generateRandomNumCard = new GenerateRandomNum();

        Card newCard = new Card(client,
                CardType.valueOf(addCardDTO.type()),
                CardColor.valueOf(addCardDTO.color()),
                generateRandomNumCard.getRandomNumber(1000,10000)+"-"+generateRandomNumCard.getRandomNumber(1000,10000)+"-"+generateRandomNumCard.getRandomNumber(1000,10000)+"-"+generateRandomNumCard.getRandomNumber(1000,10000),
                generateRandomNumCard.getRandomNumber(100,1000),
                LocalDate.now().plusYears(5) ,
                LocalDate.now());

        cardRepository.save(newCard);
        client.addCards(newCard);
        clientRepository.save(client);


        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }
}
