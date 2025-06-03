package com.rejabsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.repo.BoardRepository;
import com.rejabsbackend.service.AuthService;
import com.rejabsbackend.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    private final String BOARD_ID = "board123";
    private final String USER_ID = "user123";

//    @Autowired
//    private BoardService boardService;
//
//    @Autowired
//    private AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper();


//    String boardJson = """
//                        {
//                            "id": "1",
//                            "title": "Project A",
//                            "ownerId": "owner123",
//                            "collaborators": ["user1", "user2"]
//                        }
//            """;
//
//    String boardDtoJson= """
//                        {
//                            "title": "Project A",
//                            "ownerId": "owner123",
//                            "collaborators": ["user1", "user2"]
//                        }
//            """;
//
//    String boardArray= """
//                      [{
//                            "id": "1",
//                            "title": "Project A",
//                            "ownerId": "owner123",
//                            "collaborators": ["user1", "user2"]
//                        }]
//            """;



    Board board= new Board ("1", "Project A", "owner123", List.of("user1", "user2"));

    BoardDto boardDto = new BoardDto("Project A","owner123", List.of("user1", "user2"));


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
    void getBoardById_shouldReturnBoard_whenCalledWithValidId() throws Exception{
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards"+board.boardId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(board)));
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledWithInvalidId() throws Exception{
        boardRepository.save(board);
        mockMvc.perform(get("/api/boards"+board.boardId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("id not found"));
    }


    @Test
    void createBoard_shouldReturnNewBoard_whenCalledWithBoardDto() throws Exception{

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(board)));
    }

    @Test
    void updateBoard_shouldReturnBoard_whenCalledWithValidBoardDto() throws Exception{
        BoardDto updatedBoardDto = new BoardDto("Project A schema", board.ownerId(), board.collaborators());
        boardRepository.save(board);
        //When
        mockMvc.perform(put("/api/boards/"+board.boardId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBoardDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").isNotEmpty())
                .andExpect(jsonPath("$.title").value(updatedBoardDto.title()))
                .andExpect(jsonPath("$.ownerId").value(board.ownerId()))
                .andExpect(jsonPath("$.collaborators").value(board.collaborators()));
    }

    @Test
    void deleteBoard_shouldDeleteBoard_whenCalledWithIValidId() throws Exception{
        boardRepository.save(board);
        //when
        mockMvc.perform(delete("/api/boards/"+board.boardId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoard_shouldThrowException_whenCalledWithInvalidId() throws Exception{
        String invalidId = "xyz22";
        //when
        mockMvc.perform(delete("/api/boards/"+invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Error: Board Id " + invalidId + " not found"));
    }
}