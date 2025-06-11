package com.rejabsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.Collaborator;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.repo.BoardRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    AppUser user;
    Board board;
    BoardDto boardDto;
    BoardDto updateDto;
    Board updatedBoard;


    @BeforeEach
    void setUp() {
        user = new AppUser(1234, "testUser", "mock@example.com", "http://image.png");

        board = new Board("board123", "Project A", "1234", List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for Create Method
        boardDto = new BoardDto("Project A", List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for Update method
        updateDto = new BoardDto("Project A schema", board.collaborators());
        updatedBoard = new Board("board123", "Project A schema", board.ownerId(), board.collaborators());

        appUserRepository.save(user);

    }

    protected RequestPostProcessor getOidcLogin() {
        return oidcLogin().userInfoToken(token -> token
                .claim("id", 1234)
                .claim("login", "testUser")
                .claim("email", "mock@example.com")
                .claim("avatar_url", "http://image.png")
        );
    }


    @Test
    void getAllBoards_shouldReturnListOfBoards_whenCalled() throws Exception {
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(board))));
    }

    @Test
    void getAllBoards_shouldReturnEmptyList_whenCalled() throws Exception {
        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBoardById_shouldReturnBoard_whenCalledWithValidId() throws Exception {
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards/"+board.boardId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(board)));
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        mockMvc.perform(get("/api/boards/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board Id " + invalidId + " not found"));
    }


    @Test
    void createBoard_shouldReturnNewBoard_whenCalledWithBoardDto() throws Exception {
        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
                        .with(getOidcLogin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").isNotEmpty())
                .andExpect(content().json(objectMapper.writeValueAsString(boardDto)));

    }

    @Test
    void updateBoard_shouldReturnBoard_whenCalledWithValidBoardDto() throws Exception {
        boardRepository.save(board);
        //When
        mockMvc.perform(put("/api/boards/"+board.boardId())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(getOidcLogin()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedBoard)));
    }

    @Test
    void deleteBoard_shouldDeleteBoard_whenCalledWithValidId() throws Exception {
        boardRepository.save(board);
        //when
        mockMvc.perform(delete("/api/boards/" + board.boardId())
                        .with(getOidcLogin()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoard_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        //when
        mockMvc.perform(delete("/api/boards/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board Id " + invalidId + " not found"));
    }
}