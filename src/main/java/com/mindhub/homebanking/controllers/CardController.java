package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
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
    private ResponseEntity<?> addCard(@RequestBody CardDTO cardDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(email);

        Set<Card> cards = client.getCards();

        Set<Boolean> existTypeColor = cards
                                    .stream()
                                    .map(card -> (card.getType() == cardDTO.getType() && card.getColor() == cardDTO.getColor()))
                                    .collect(Collectors.toSet());

        if (existTypeColor.contains(true)){
            return new ResponseEntity<>("You already have a card of type " + cardDTO.getType().toString().toLowerCase() +" with the color " + cardDTO.getColor().toString().toLowerCase(), HttpStatus.FORBIDDEN);
        }

        /*Set<Card> cardDebit = cards.stream().filter(card -> Objects.equals(card.getType().toString(), "DEBIT")).collect(Collectors.toSet());

        if (cardDebit.size() == 3){
            return new ResponseEntity<>("The maximum number of debit cards allowed has been reached", HttpStatus.FORBIDDEN);
        }

        Set<Card> cardCredit = cards.stream().filter(card -> Objects.equals(card.getType().toString(), "CREDIT")).collect(Collectors.toSet());

        if (cardCredit.size() == 3){
            return new ResponseEntity<>("he maximum number of credit cards allowed has been reached", HttpStatus.FORBIDDEN);
        }*/

        GenerateRandomNum generateRandomNumCard = new GenerateRandomNum();

        Card newCard = new Card(client,
                cardDTO.getType(),
                cardDTO.getColor(),
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
