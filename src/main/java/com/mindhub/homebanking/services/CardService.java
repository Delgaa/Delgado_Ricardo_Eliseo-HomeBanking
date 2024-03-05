package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface CardService {

    List<Card> getCardsByCardHolder(String cardHolder);

    List<CardDTO> getCardsDTO(String cardHolder);

    int countCardsByTypeAndClient(String type, Client client);

    Boolean CardExistsByTypeAndColorAndClient(String type, String color ,Client client);

    void saveCard(Card card);
}
