package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.exception.UnAuthorizedUserException;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.repo.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.annotation.Id;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

class BoardServiceTest {


    BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
    AuthService authService = Mockito.mock(AuthService.class);
    IdService idService = Mockito.mock(IdService.class);
    BoardService boardService;

    private Board board;
    private BoardDto createBoardDto;
    private BoardDto boardDto;
    private Board updatedBoard;
    private final String BOARD_ID = "board123";
    private final String USER_ID = "user123";

    @BeforeEach
    void setUp() {

        board = new Board(BOARD_ID, "Original Title", USER_ID, List.of("user1", "user2"));

        // for CreateBoard Method
        createBoardDto = new BoardDto("Original Title", USER_ID, List.of("user1", "user2"));

        // for UpdateBoard method
        boardDto = new BoardDto("Updated Title", USER_ID, List.of("user3", "user4"));

        updatedBoard = new Board(BOARD_ID, "Updated Title", USER_ID, List.of("user3", "user4"));
        boardService = new BoardService(boardRepository, idService, authService);

    }


    @Test
    void getAllBoards_shouldReturnEmptyList_whenCalled() {
        // GIVEN
        //in setup

        // WHEN
        Mockito.when(boardRepository.findAll()).thenReturn(Collections.emptyList());
        List<Board> actual = boardService.getAllBoards();

        // THEN
        assertEquals(Collections.emptyList(), actual);
    }


    @Test
    void getAllBoards_shouldReturnBoardList_whenCalled() {
        // GIVEN
        List<Board> expected = List.of(board);
        //WHEN

        Mockito.when(boardRepository.findAll()).thenReturn(expected);
        List<Board> actual = boardService.getAllBoards();

        //THEN
        assertEquals(expected, actual);

    }

    @Test
    void getBoardById_shouldReturnBoard_whenCalledByValidId() throws IdNotFoundException {
        // GIVEN

        // WHEN
        boardRepository.save(board);
        Mockito.when(boardRepository.findById(board.boardId())).thenReturn(Optional.of(board));
        Board actual = boardService.getBoardById(board.boardId());

        // THEN
        assertEquals(board, actual);
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledByInvalidId() {
        Mockito.when(boardRepository.findById("wrongID")).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> boardService.getBoardById("wrongID"));
    }

    @Test
    void createBoard_shouldReturnBoard_whenCalledWithValidData() {
        // Given
        Mockito.when(idService.generateId()).thenReturn("board123");
        Mockito.when(boardRepository.save(board)).thenReturn(board);
        Mockito.when(authService.getCurrentUserId()).thenReturn(USER_ID);

        // When
        Board actual = boardService.createBoard(createBoardDto);

        // Then
        assertEquals(board, actual);
        Mockito.verify(boardRepository, Mockito.times(1)).save(board);
    }

    @Test
    void updateBoard_shouldUpdateBoard_whenUserIsOwnerAndBoardFound() throws IdNotFoundException, UnAuthorizedUserException {
        //Given

        // when
        Mockito.when(authService.getCurrentUserId()).thenReturn(USER_ID);
        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        Mockito.when(boardRepository.save(updatedBoard)).thenReturn(updatedBoard);

        // then
        Board actualBoard = boardService.updateBoard(BOARD_ID, boardDto);
        assertNotNull(actualBoard);
        assertEquals(updatedBoard, actualBoard);

        Mockito.verify(boardRepository).findById(BOARD_ID);
        Mockito.verify(boardRepository).save(updatedBoard);
        Mockito.verify(authService).getCurrentUserId();
    }


    @Test
    void updateBoard_shouldThrowIdNotFoundException_whenBoardNotFound() throws IdNotFoundException {

        //when
        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(IdNotFoundException.class, () -> boardService.updateBoard(BOARD_ID, boardDto));

        Mockito.verify(boardRepository).findById(BOARD_ID);
    }

    @Test
    void updateBoard_shouldThrowUnauthorizedException_whenUserNotOwner() throws UnAuthorizedUserException {

        Board boardWithDifferentOwner = new Board(BOARD_ID, "Original Title", "differentUser", List.of("user1", "user2"));

        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(boardWithDifferentOwner));
        Mockito.when(authService.getCurrentUserId()).thenReturn(USER_ID);

        assertThrows(UnAuthorizedUserException.class, () -> boardService.updateBoard(BOARD_ID, boardDto));

        Mockito.verify(boardRepository).findById(BOARD_ID);
    }


    @Test
    void deleteBoardById_shouldDeleteBoard_whenCalledWithValidId() throws IdNotFoundException {

        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(USER_ID);

        boolean actualBoard = boardService.deleteBoardById(BOARD_ID);

        // then
        assertTrue(actualBoard);

        Mockito.verify(boardRepository).findById(BOARD_ID);
        Mockito.verify(boardRepository).delete(board);
        Mockito.verify(authService).getCurrentUserId();

    }

    @Test
    void deleteBoardById_shouldThrowIdNotFoundException_whenCalledWithInvalidId() throws IdNotFoundException {
        //when
        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        //then
        assertThrows(IdNotFoundException.class, () -> boardService.deleteBoardById(BOARD_ID));

        Mockito.verify(boardRepository).findById(BOARD_ID);
    }

    @Test
    void deleteBoardById_shouldThrowUnAuthorizedUserException_whenCalledWithInvalidOwner() {
        Mockito.when(authService.getCurrentUserId()).thenReturn(USER_ID);

        assertThrows(UnAuthorizedUserException.class, () -> boardService.deleteBoardById(BOARD_ID));
        Mockito.verify(authService).getCurrentUserId();

    }


    @Test
    void getBoardIfOwner_shouldThrownException_whenCalledWithValidId() throws IdNotFoundException, UnAuthorizedUserException {
        Mockito.when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(null);

        assertThrows(IdNotFoundException.class, () -> boardService.getBoardIfOwner(BOARD_ID));
        assertThrows(UnAuthorizedUserException.class, () -> boardService.getBoardIfOwner(BOARD_ID));

        // Verify
        Mockito.verify(boardRepository).findById(BOARD_ID);
        Mockito.verify(authService).getCurrentUserId();
    }
}