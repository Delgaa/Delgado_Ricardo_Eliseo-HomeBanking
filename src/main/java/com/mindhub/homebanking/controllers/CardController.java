package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.services.CardVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {


    @Autowired
    private CardVerificationService cardVerificationService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCards(){
        return cardVerificationService.verifyGetAllCards();
    }

    @PostMapping("/")
    public ResponseEntity<?> addCard(@RequestBody CardApplicationDTO cardApplicationDTO){
        return cardVerificationService.verifyCardApplication(cardApplicationDTO);
    }
}
