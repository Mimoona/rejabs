package com.rejabsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rejabsbackend.dto.CardDto;
import com.rejabsbackend.enums.Label;
import com.rejabsbackend.model.Card;
import com.rejabsbackend.repo.CardRepository;
import com.rejabsbackend.service.IdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private IdService idService;

    @Autowired
    private ObjectMapper mapper;

   Card card;
   CardDto cardDto;
   CardDto updateCardDto;
   Card updatedCard;
   Instant fixedInstant = Instant.parse("2025-06-19T20:42:24.491543600Z");
    List<Label> labels = Arrays.asList(
            Label.HIGH_PRIORITY,
            Label.BACKEND,
            Label.IN_PROGRESS
    );

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        card = new Card("123a",
                "New Card",
                "this is card",
                "999",
                0,
                labels,
                LocalDate.of(2025, 6, 27),
                fixedInstant,
                fixedInstant);
        // For Create
        cardDto = new CardDto("New Card",
                "this is card",
                "999",
                0,
                labels,
                LocalDate.of(2025, 6, 27),
                fixedInstant,
                fixedInstant);
        //For Update
        updateCardDto = new CardDto("New Card title updated",
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
    void testSerializationOfLocalDate() throws Exception {
        Card card = new Card("123a",
                "New Card",
                "this is card",
                "999",
                0,
                labels,
                LocalDate.of(2025, 6, 27),
                fixedInstant,
                fixedInstant);
        String json = mapper.writeValueAsString(card);
        System.out.println(json);

        Card parsed = mapper.readValue(json, Card.class);
        assertEquals(LocalDate.of(2025, 6, 27), parsed.dueDate());
    }


    @Test
    void getAllCards_shouldReturnCards_whenCalled() throws Exception {
        cardRepository.save(card);
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardId").value(card.cardId()))
                .andExpect(jsonPath("$[0].cardTitle").value(card.cardTitle()))
                .andExpect(jsonPath("$[0].description").value(card.description()))
                .andExpect(jsonPath("$[0].listId").value(card.listId()))
                .andExpect(jsonPath("$[0].position").value(card.position()))
                .andExpect(jsonPath("$[0].labels").isArray())
                .andExpect(jsonPath("$[0].dueDate").value(card.dueDate().toString()))
                .andExpect(jsonPath("$[0].createdAt").value(card.createdAt().truncatedTo(ChronoUnit.MILLIS).toString()))
                .andExpect(jsonPath("$[0].updatedAt").value(card.updatedAt().truncatedTo(ChronoUnit.MILLIS).toString()));
    }
    @Test
    void getAllCards_shouldReturnEmptyList_whenCalled() throws Exception {
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getCardById_shouldReturnCard_whenCalledWithValidId() throws Exception {
        cardRepository.save(card);
        mockMvc.perform(get("/api/cards/"+card.cardId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardId").value(card.cardId()))
                .andExpect(jsonPath("$.cardTitle").value(card.cardTitle()))
                .andExpect(jsonPath("$.description").value(card.description()))
                .andExpect(jsonPath("$.listId").value(card.listId()))
                .andExpect(jsonPath("$.position").value(card.position()))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.dueDate").value(card.dueDate().toString()))
                .andExpect(jsonPath("$.createdAt").value(card.createdAt().truncatedTo(ChronoUnit.MILLIS).toString()))
                .andExpect(jsonPath("$.updatedAt").value(card.updatedAt().truncatedTo(ChronoUnit.MILLIS).toString()));
    }
    @Test
    void getCardById_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        mockMvc.perform(get("/api/cards/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Card Id " + invalidId + " not found"));
    }


    @Test
    void createCard_shouldReturnNewCard_whenCalledWithValidData() throws Exception {
        mockMvc.perform(post("/api/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardId").isNotEmpty())
                .andExpect(jsonPath("$.cardTitle").value(cardDto.cardTitle()))
                .andExpect(jsonPath("$.description").value(cardDto.description()))
                .andExpect(jsonPath("$.listId").value(cardDto.listId()))
                .andExpect(jsonPath("$.position").value(cardDto.position()))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.dueDate").value(cardDto.dueDate().toString()))
                .andExpect(jsonPath("$.cardId").isNotEmpty());
    }

    @Test
    void updateCard_shouldReturnBoard_whenCalledWithValidCardDto() throws Exception {
        cardRepository.save(card);
        //When
        mockMvc.perform(put("/api/cards/"+card.cardId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateCardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardTitle").value(updateCardDto.cardTitle()))
                .andExpect(jsonPath("$.description").value(updateCardDto.description()))
                .andExpect(jsonPath("$.listId").value(card.listId()))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.dueDate").value(card.dueDate().toString()))
                .andExpect(jsonPath("$.cardId").value(card.cardId()));
    }

    @Test
    void deleteCard_shouldDeleteCard_whenCalledWithValidId() throws Exception  {
        cardRepository.save(card);
        //when
        mockMvc.perform(delete("/api/cards/" + card.cardId()))
                .andExpect(status().isOk());
    }
    @Test
    void deleteCard_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        //when
        mockMvc.perform(delete("/api/cards/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Card Id " + invalidId + " not found"));
    }
}