package com.rejabsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.dto.BoardListDto;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.BoardList;
import com.rejabsbackend.model.Collaborator;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.repo.BoardListRepository;
import com.rejabsbackend.repo.BoardRepository;
import com.rejabsbackend.service.BoardListService;
import com.rejabsbackend.service.IdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardListRepository boardListRepository;

    @Autowired
    private IdService idService;
    ObjectMapper objectMapper = new ObjectMapper();

    BoardList boardList;
    BoardListDto boardListDto;
    BoardListDto updateBoardListDto;
    BoardList updatedBoardList;

    @BeforeEach
    void setUp() {

        boardList = new BoardList("123a", "List title", "board123", 0);
        // For Create
        boardListDto = new BoardListDto("List title", "board123", 0);
        //For Update
        updateBoardListDto = new BoardListDto("new title", "board123", 1);
        updatedBoardList = new BoardList("123a", "new title", "board123", 1);
    }

    @Test
    void getAllBoardLists_shouldReturnBoardLists_whenCalled() throws Exception{
        boardListRepository.save(boardList);
        mockMvc.perform(get("/api/board-list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(boardList))));
    }

    @Test
    void getAllBoardLists_shouldReturnEmptyList_whenCalled() throws Exception {
        mockMvc.perform(get("/api/board-list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBoardListById_shouldReturnBoardList_whenCalledWithValidId() throws Exception{
        boardListRepository.save(boardList);
        mockMvc.perform(get("/api/board-list/"+boardList.boardListId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardList)));
    }

    @Test
    void getBoardListById_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        mockMvc.perform(get("/api/board-list/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board List Id " + invalidId + " not found"));
    }


    @Test
    void createBoardList_shouldReturnNewBoardList_whenCalledWithValidData() throws Exception{
        mockMvc.perform(post("/api/board-list/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardListId").isNotEmpty())
                .andExpect(content().json(objectMapper.writeValueAsString(boardListDto)));
    }

    @Test
    void updateBoardList_shouldReturnBoard_whenCalledWithValidBoardListDto() throws Exception{
        boardListRepository.save(boardList);
        //When
        mockMvc.perform(put("/api/board-list/"+boardList.boardListId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBoardListDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updateBoardListDto)));
    }

    @Test
    void deleteBoardList_shouldDeleteBoardList_whenCalledWithValidId() throws Exception {
        boardListRepository.save(boardList);
        //when
        mockMvc.perform(delete("/api/board-list/" + boardList.boardListId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoardList_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        //when
        mockMvc.perform(delete("/api/board-list/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board List Id " + invalidId + " not found"));
    }
}