package com.rejabsbackend.service;

import com.rejabsbackend.dto.CardDto;
import com.rejabsbackend.enums.Label;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Card;
import com.rejabsbackend.repo.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {
    CardRepository cardRepository = Mockito.mock(CardRepository.class);
    IdService idService = Mockito.mock(IdService.class);
    CardService cardService;
    private Card card;
    private CardDto cardDto;
    private CardDto updateDto;
    private Card updatedCard;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, idService);
        Instant fixedInstant = Instant.parse("2025-06-19T20:42:24.491543600Z");
        List<Label> labels = Arrays.asList(
                Label.HIGH_PRIORITY,
                Label.BACKEND,
                Label.IN_PROGRESS
        );
        card = new Card("123a",
                "New Card",
                "this is card",
                "999",
                0,
                labels,
                LocalDate.now().plusDays(7),
                fixedInstant,
                fixedInstant);
        // For Create
        cardDto = new CardDto("New Card",
                "this is card",
                "999",
                0,
                labels,
                LocalDate.now().plusDays(7),
                fixedInstant,
                fixedInstant);
        //For Update
        updateDto = new CardDto("New Card title updated",
                "this is card new description",
                null,
                0,
               null,
                null,
                null,
                null);
        updatedCard = new Card("123a",
                "New Card title updated",
                "this is card new description",
                card.listId(),
                0,
                card.labels(),
                card.dueDate(),
                card.createdAt(),
                fixedInstant);

    }


    @Test
    void getAllCards_shouldReturnEmptyList_whenCalled() {
        // WHEN
        Mockito.when(cardRepository.findAll()).thenReturn(Collections.emptyList());
        List<Card> actual = cardService.getAllCards();

        // THEN
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void getAllBoardLists_shouldReturnBoardList_whenCalled() {
        // GIVEN
        List<Card> expected = List.of(card);

        //WHEN
        Mockito.when(cardRepository.findAll()).thenReturn(expected);
        List<Card> actual = cardService.getAllCards();

        //THEN
        assertEquals(expected, actual);

    }


    @Test
    void getCardById_shouldReturnCard_whenCalledByValidId() throws IdNotFoundException {
        // WHEN
        cardRepository.save(card);
        Mockito.when(cardRepository.findById(card.cardId())).thenReturn(Optional.of(card));
        Card actual = cardService.getCardById(card.cardId());

        // THEN
        assertEquals(card, actual);
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledByInvalidId() {
        Mockito.when(cardRepository.findById("wrongID")).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> cardService.getCardById("wrongID"));
    }

    @Test
    void createCard_shouldReturnBoardList_whenCalledWithValidData() {
        // When
        Mockito.when(idService.generateId()).thenReturn("123a");
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(card);

        // Then
        Card actual = cardService.createCard(cardDto);

        Mockito.verify(idService, Mockito.times(1)).generateId();
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any(Card.class));

        assertEquals(card, actual);

    }

    @Test
    void updateCard_shouldUpdateCard_whenCalledWithValidCardId() throws IdNotFoundException {
        // when
        Mockito.when(cardRepository.findById(card.cardId())).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(updatedCard);

        // then
        Card actual = cardService.updateCard(card.cardId(), updateDto);
//        assertNotNull(actual);
        assertEquals(updatedCard, actual);



        Mockito.verify(cardRepository).findById(card.cardId());
        Mockito.verify(cardRepository).save(Mockito.any(Card.class));
    }

    @Test
    void updateBoard_shouldThrowIdNotFoundException_whenBoardNotFound() {

        //when
        Mockito.when(cardRepository.findById(card.cardId())).thenReturn(Optional.empty());

        //then
        assertThrows(IdNotFoundException.class, () -> cardService.updateCard(card.cardId(), cardDto));

        Mockito.verify(cardRepository).findById(card.cardId());
    }

    @Test
    void deleteCardById_shouldDeleteBoard_whenCalledWithValidId() throws IdNotFoundException {
        Mockito.when(cardRepository.existsById(card.cardId())).thenReturn(true);
        boolean actualCard = cardService.deleteCardById(card.cardId());

        // then
        assertTrue(actualCard);

        Mockito.verify(cardRepository).existsById(card.cardId());
        Mockito.verify(cardRepository).deleteById(card.cardId());
    }

    @Test
    void deleteBoardListById_shouldThrowIdNotFoundException_whenCalledWithInvalidId() throws IdNotFoundException {
        String notFoundCardId = "missing-123";
        //when
        Mockito.when(cardRepository.existsById(notFoundCardId)).thenReturn(false);

        //then
        assertThrows(IdNotFoundException.class, () -> cardService.deleteCardById(notFoundCardId));

        Mockito.verify(cardRepository).existsById(notFoundCardId);
    }
}