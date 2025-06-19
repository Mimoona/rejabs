package com.rejabsbackend.service;

import com.rejabsbackend.dto.CardDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Card;
import com.rejabsbackend.repo.CardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final IdService idService;

    public CardService(CardRepository cardRepository, IdService idService) {
        this.cardRepository = cardRepository;
        this.idService = idService;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(String cardId) throws IdNotFoundException {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IdNotFoundException(cardId, "Card"));

    }

    public Card createCard(CardDto cardDto) {
        Card card = new Card(
                idService.generateId(),
                cardDto.cardTitle(),
                cardDto.description(),
                cardDto.listId(),
                cardDto.position(),
                cardDto.labels(),
                cardDto.dueDate(),
                Instant.now(),
                Instant.now()
        );

        return cardRepository.save(card);
    }

    public Card updateCard(String cardId, CardDto cardDto) throws IdNotFoundException{
            Card existingCard = cardRepository.findById(cardId)
                    .orElseThrow(() -> new IdNotFoundException(cardId, "Card"));

            if (existingCard == null) {
                return null;
            }

        Card updatedCard = new Card(
                existingCard.cardId(),
                cardDto.cardTitle() != null ? cardDto.cardTitle() : existingCard.cardTitle(),
                cardDto.description() != null ? cardDto.description() : existingCard.description(),
                cardDto.listId() != null ? cardDto.listId() : existingCard.listId(),
                cardDto.position() ,
                cardDto.labels() != null ? cardDto.labels() : existingCard.labels(),
                cardDto.dueDate() != null ? cardDto.dueDate() : existingCard.dueDate(),
                existingCard.createdAt(),
                Instant.now()

        );

        return cardRepository.save(updatedCard);
    }

    public boolean deleteCardById(String cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
            return true;
        } else {
            return false;
        }
    }
}
