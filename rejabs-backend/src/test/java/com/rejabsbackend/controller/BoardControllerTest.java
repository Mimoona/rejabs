package com.rejabsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.enums.AuthProvider;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.Collaborator;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.repo.BoardRepository;

import com.rejabsbackend.service.IdService;
import com.rejabsbackend.testsupport.SecurityTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private IdService idService;

    ObjectMapper objectMapper = new ObjectMapper();
    AppUser user;
    Board board;
    BoardDto boardDto;
    BoardDto updateDto;
    Board updatedBoard;


    @BeforeEach
    void setUp() {
        user = new AppUser("1234", "testUser", "mock@example.com", "password","http://image.png", AuthProvider.GITHUB);

        board = new Board("board123", "Project A", "12345", List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for Create Method
        boardDto = new BoardDto("Project A", List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for Update method
        updateDto = new BoardDto("Project A schema", boardDto.collaborators());
        updatedBoard = new Board("board123", "Project A schema", board.ownerId(), board.collaborators());

        appUserRepository.save(user);

    }

    @Test
    void getAllBoards_shouldReturnListOfBoards_whenCalled() throws Exception {
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards")
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(board))));
    }

    @Test
    void getAllBoards_shouldReturnEmptyList_whenCalled() throws Exception {
        mockMvc.perform(get("/api/boards")
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBoardById_shouldReturnBoard_whenCalledWithValidId() throws Exception {
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards/"+board.boardId())
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(board)));
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        mockMvc.perform(get("/api/boards/"+invalidId)
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board Id " + invalidId + " not found"));
    }


    @Test
    void createBoard_shouldReturnNewBoard_whenCalledWithBoardDto() throws Exception {

       MvcResult result =  mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto))
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").isNotEmpty())
                .andExpect(jsonPath("$.collaborators[0].collaboratorId").isNotEmpty())
                .andReturn();
//                .andExpect(content().json(objectMapper.writeValueAsString(boardDto)));

        String responseContent = result.getResponse().getContentAsString();
        Board responseBoard = objectMapper.readValue(responseContent, Board.class);

        // Compare only the relevant fields, ignoring IDs
        assertEquals(boardDto.title(), responseBoard.title());
        assertEquals(boardDto.collaborators().get(0).collaboratorName(),
                responseBoard.collaborators().get(0).collaboratorName());
        assertEquals(boardDto.collaborators().get(0).collaboratorEmail(),
                responseBoard.collaborators().get(0).collaboratorEmail());
        assertEquals(boardDto.collaborators().get(0).collaboratorAvatar(),
                responseBoard.collaborators().get(0).collaboratorAvatar());

}

    @Test
    void updateBoard_shouldReturnBoard_whenCalledWithValidBoardDto() throws Exception {
        boardRepository.save(board);
        //When
        mockMvc.perform(put("/api/boards/"+board.boardId())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedBoard)));
    }

    @Test
    void deleteBoard_shouldDeleteBoard_whenCalledWithValidId() throws Exception {
        boardRepository.save(board);
        //when
        mockMvc.perform(delete("/api/boards/" + board.boardId())
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoard_shouldThrowException_whenCalledWithInvalidId() throws Exception {
        String invalidId = "xyz22";
        //when
        mockMvc.perform(delete("/api/boards/"+invalidId)
                        .with(SecurityTestSupport.getOAuthLogin()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Board Id " + invalidId + " not found"));
    }
}