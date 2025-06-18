package com.rejabsbackend.controller;

import com.rejabsbackend.dto.CardDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Card;
import com.rejabsbackend.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }


    @GetMapping("/{cardId}")
    public Card getCardById(@PathVariable String cardId) throws IdNotFoundException {
        return cardService.getCardById(cardId);

    }

    @PostMapping
    public Card createCard(@RequestBody CardDto cardDto) {
        return cardService.createCard(cardDto);
    }

    @PutMapping("/{cardId}")
    public Card updateCard(@PathVariable String cardId, @RequestBody CardDto cardDto) throws IdNotFoundException{
        return cardService.updateCard(cardId, cardDto);
    }


    @DeleteMapping("/{cardId}")
    public boolean deleteCard(@PathVariable String cardId) throws IdNotFoundException {
        return cardService.deleteCardById(cardId);
    }



}
