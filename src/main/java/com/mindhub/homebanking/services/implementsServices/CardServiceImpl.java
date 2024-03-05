package com.mindhub.homebanking.services.implementsServices;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> getCardsByCardHolder(String cardHolder) {
        return cardRepository.findByCardHolder(cardHolder);
    }

    @Override
    public List<CardDTO> getCardsDTO(String cardHolder) {
        return getCardsByCardHolder(cardHolder).stream().map(CardDTO::new).toList();
    }

    @Override
    public int countCardsByTypeAndClient(String type, Client client) {
        return cardRepository.countByTypeAndClient(CardType.valueOf(type), client);
    }

    @Override
    public Boolean CardExistsByTypeAndColorAndClient(String type, String color, Client client) {
        return cardRepository.existsCardByTypeAndColorAndClient(CardType.valueOf(type), CardColor.valueOf(color), client);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
