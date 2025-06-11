package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.Collaborator;
import com.rejabsbackend.repo.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;

class BoardServiceTest {


    BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
    AuthService authService = Mockito.mock(AuthService.class);
    IdService idService = Mockito.mock(IdService.class);
    BoardService boardService;

    private Board board;
    private BoardDto createBoardDto;
    private BoardDto boardDto;
    private Board updatedBoard;
    private final String boardId = "board123";
    private final String userId = "user123";
    private static final String DIFFERENT_USER_ID = "differentUser123";

    @BeforeEach
    void setUp() {

        board = new Board(boardId, "Original Title", userId , List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for CreateBoard Method
        createBoardDto = new BoardDto("Original Title", List.of(
                new Collaborator("collab1", "John Doe", "john@example.com", "avatar1.jpg")
        ));

        // for UpdateBoard method
        boardDto = new BoardDto("Updated Title", List.of(
                new Collaborator("collab2", "Max", "max@example.com", "avatar2.jpg")
        ));

        updatedBoard = new Board(boardId, "Updated Title", userId , List.of(
                new Collaborator("collab2", "Max", "max@example.com", "avatar2.jpg")
        ));
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
        // When
        Mockito.when(idService.generateId())
                .thenReturn("collab1")
                .thenReturn("board123");


        Mockito.when(boardRepository.save(board)).thenReturn(board);
        Mockito.when(authService.getCurrentUserId()).thenReturn(userId );

        // Then
        Board actual = boardService.createBoard(createBoardDto);

        Mockito.verify(idService, Mockito.times(2)).generateId();
        Mockito.verify(boardRepository, Mockito.times(1)).save(board);
        assertEquals(board, actual);

    }

    @Test
    void updateBoard_shouldUpdateBoard_whenUserIsOwnerAndBoardFound() throws IdNotFoundException, AuthenticationException {
        //Given

        // when
        Mockito.when(authService.getCurrentUserId()).thenReturn(userId );
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(boardRepository.save(updatedBoard)).thenReturn(updatedBoard);

        // then
        Board actualBoard = boardService.updateBoard(boardId, boardDto);
        assertNotNull(actualBoard);
        assertEquals(updatedBoard, actualBoard);

        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(boardRepository).save(updatedBoard);
        Mockito.verify(authService).getCurrentUserId();
    }


    @Test
    void updateBoard_shouldThrowIdNotFoundException_whenBoardNotFound() {

        //when
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        //then
        assertThrows(IdNotFoundException.class, () -> boardService.updateBoard(boardId, boardDto));

        Mockito.verify(boardRepository).findById(boardId);
    }

    @Test
    void updateBoard_shouldThrowAuthenticationException_whenUserNotOwner() throws AuthenticationException {

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(DIFFERENT_USER_ID);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> boardService.updateBoard(boardId, boardDto));

        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(authService).getCurrentUserId();

        assertEquals("User is not the owner of the board", exception.getMessage());
    }

    @Test
    void updateBoard_shouldUseNewCollaborators_whenCollaboratorsProvided_() throws IdNotFoundException, AuthenticationException{
        // Given

        Board existing = new Board(boardId, "Old Title", "owner1", List.of());
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(existing));
        Mockito.when(authService.getCurrentUserId()).thenReturn("owner1");

        List<Collaborator> newCollaborators = List.of(
                new Collaborator("1", "Alice", "alice@example.com", null)
        );
        BoardDto dto = new BoardDto("Updated Title", newCollaborators);

        // When
        boardService.updateBoard(boardId, dto);

        // Then
        Mockito.verify(boardRepository).save(argThat(updated ->
                updated.collaborators().equals(newCollaborators)
        ));

    }

    @Test
    void updateBoard_shouldRetainExistingCollaborators_whenCollaboratorsNull() throws IdNotFoundException{
        // Given

        List<Collaborator> originalCollaborators = List.of(
                new Collaborator("2", "Bob", "bob@example.com", null)
        );
        Board existing = new Board(boardId, "Old Title", "owner1", originalCollaborators);
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(existing));
        Mockito.when(authService.getCurrentUserId()).thenReturn("owner1");

        BoardDto dto = new BoardDto("Updated Title", null); // collaborators null

        // When
        boardService.updateBoard(boardId, dto);

        // Then
        Mockito.verify(boardRepository).save(argThat(updated ->
                updated.collaborators().equals(originalCollaborators)
        ));
    }
    @Test
    void updateBoard_shouldRetainExistingTitle_whenTitleNull() throws IdNotFoundException{
        // Given
        List<Collaborator> originalCollaborators = List.of(
                new Collaborator("2", "Bob", "bob@example.com", null)
        );
        Board existing = new Board(boardId, "existingTitle", "owner1", originalCollaborators);
        BoardDto dto = new BoardDto(null, originalCollaborators);

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(existing));
        Mockito.when(authService.getCurrentUserId()).thenReturn("owner1");

        // When
        boardService.updateBoard(boardId, dto);

        // Then
        Mockito.verify(boardRepository).save(argThat(updated ->
                updated.title().equals("existingTitle")
        ));
    }


    @Test
    void deleteBoardById_shouldDeleteBoard_whenCalledWithValidId() throws IdNotFoundException {

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(userId );

        boolean actualBoard = boardService.deleteBoardById(boardId);

        // then
        assertTrue(actualBoard);

        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(boardRepository).delete(board);
        Mockito.verify(authService).getCurrentUserId();

    }

    @Test
    void deleteBoardById_shouldThrowIdNotFoundException_whenCalledWithInvalidId() {
        String notFoundBoardId = "missing-123";
        //when
        Mockito.when(boardRepository.findById(notFoundBoardId)).thenReturn(Optional.empty());
        //then
        assertThrows(IdNotFoundException.class, () -> boardService.deleteBoardById(boardId));

        Mockito.verify(boardRepository).findById(boardId);
    }

    @Test
    void deleteBoardById_shouldThrowAuthenticationException_whenCalledWithInvalidOwner() throws AuthenticationException{
        String invalidUser = "invalidUser";
        Mockito.when(boardRepository.findById(board.boardId())).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(invalidUser);

        Mockito.verify(boardRepository, never()).deleteById(boardId);
        assertThrows(AuthenticationException.class, () -> boardService.deleteBoardById(boardId));


    }


    @Test
    void getBoardIfOwner_shouldThrownException_whenCalledWithValidBoardId() {
        String differentOwner = "Differnt owner";
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(differentOwner);

        AuthenticationException e= assertThrows(AuthenticationException.class, () -> boardService.getBoardIfOwner(boardId));
        assertEquals("User is not the owner of the board", e.getMessage());

        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(authService).getCurrentUserId();
    }


    @Test
    void getBoardIfOwner_shouldReturnBoard_whenUserIsOwner() throws IdNotFoundException{
        // when
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(authService.getCurrentUserId()).thenReturn(userId );

        Board result = boardService.getBoardIfOwner(boardId);

        // then
        assertNotNull(result);
        assertEquals(boardId, result.boardId());
        assertEquals(userId , result.ownerId());

        // Verify
        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(authService).getCurrentUserId();
    }

    @Test
    void getBoardIfOwner_shouldThrowNotFoundException_whenBoardNotFound() {
        // When
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> boardService.getBoardIfOwner(boardId));

        // Verify
        Mockito.verify(boardRepository).findById(boardId);
        Mockito.verify(authService, never()).getCurrentUserId();
    }
}