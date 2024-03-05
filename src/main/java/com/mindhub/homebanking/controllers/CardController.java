package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCards(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        return new ResponseEntity<>(cardService.getCardsDTO(client.getName() + " " + client.getLastName()), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> addCard(@RequestBody CardApplicationDTO cardApplicationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(email);

        if (cardApplicationDTO.type().isBlank()){
            return new ResponseEntity<>("Type no content", HttpStatus.BAD_REQUEST);
        }

        if (cardApplicationDTO.color().isBlank()){
            return new ResponseEntity<>("Color no content", HttpStatus.BAD_REQUEST);
        }

        if (cardService.countCardsByTypeAndClient(cardApplicationDTO.type(), client) == 3){
            return new ResponseEntity<>("The maximum number of" +cardApplicationDTO.type().toLowerCase()+ "cards allowed has been reached", HttpStatus.FORBIDDEN);
        }

        if (cardService.CardExistsByTypeAndColorAndClient(cardApplicationDTO.type(), cardApplicationDTO.color(), client)){
            return new ResponseEntity<>("You already have a card of type " + cardApplicationDTO.type().toLowerCase() +" with the color " + cardApplicationDTO.color().toLowerCase(), HttpStatus.FORBIDDEN);
        }


        GenerateRandomNum generateRandomNumCard = new GenerateRandomNum();

        Card newCard = new Card(client,
                CardType.valueOf(cardApplicationDTO.type()),
                CardColor.valueOf(cardApplicationDTO.color()),
                generateRandomNumCard.getRandomNumberCard(),
                generateRandomNumCard.getRandomNumberCVV(),
                LocalDate.now().plusYears(5) ,
                LocalDate.now());

        cardService.saveCard(newCard);
        client.addCards(newCard);
        clientService.saveClient(client);


        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }
}
