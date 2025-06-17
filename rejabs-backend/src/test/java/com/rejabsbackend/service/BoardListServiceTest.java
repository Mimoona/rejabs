package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardListDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.BoardList;
import com.rejabsbackend.repo.BoardListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BoardListServiceTest {

    BoardListRepository boardListRepository = Mockito.mock(BoardListRepository.class);
    IdService idService = Mockito.mock(IdService.class);
    BoardListService boardListService;
    private BoardList boardList;
    private BoardListDto boardListDto;
    private BoardListDto updateDto;
    private BoardList updatedBoardList;

    @BeforeEach
    void setUp() {
        boardListService = new BoardListService(boardListRepository, idService);
        boardList = new BoardList("123a", "List title", "board123", 0);
        // For Create
        boardListDto = new BoardListDto("List title", "board123", 0);
        //For Update
        updateDto = new BoardListDto("new title", "board123", 1);
        updatedBoardList = new BoardList("123a", "new title", "board123", 1);

    }

    @Test
    void getAllBoardLists_shouldReturnEmptyList_whenCalled() {

        // WHEN
        Mockito.when(boardListRepository.findAll()).thenReturn(Collections.emptyList());
        List<BoardList> actual = boardListService.getAllBoardLists();

        // THEN
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void getAllBoardLists_shouldReturnBoardList_whenCalled() {
        // GIVEN
        List<BoardList> expected = List.of(boardList);

        //WHEN
        Mockito.when(boardListRepository.findAll()).thenReturn(expected);
        List<BoardList> actual = boardListService.getAllBoardLists();

        //THEN
        assertEquals(expected, actual);

    }

    @Test
    void getBoardListById_shouldReturnBoard_whenCalledByValidId() throws IdNotFoundException {
        // WHEN
        boardListRepository.save(boardList);
        Mockito.when(boardListRepository.findById(boardList.boardId())).thenReturn(Optional.of(boardList));
        BoardList actual = boardListService.getBoardListById(boardList.boardId());

        // THEN
        assertEquals(boardList, actual);
    }

    @Test
    void getBoardById_shouldThrowException_whenCalledByInvalidId() {
        Mockito.when(boardListRepository.findById("wrongID")).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> boardListService.getBoardListById("wrongID"));
    }

    @Test
    void createBoardList_shouldReturnBoardList_whenCalledWithValidData() {
        // When
        Mockito.when(idService.generateId()).thenReturn("123a");
        Mockito.when(boardListRepository.save(boardList)).thenReturn(boardList);

        // Then
        BoardList actual = boardListService.createBoardList(boardListDto);

        Mockito.verify(idService, Mockito.times(1)).generateId();
        Mockito.verify(boardListRepository, Mockito.times(1)).save(boardList);
        assertEquals(boardList, actual);
    }

    @Test
    void updateBoardList_shouldUpdateBoardList_whenCalledWithValidListId() throws IdNotFoundException {
        // when
        Mockito.when(boardListRepository.findById(boardList.boardListId())).thenReturn(Optional.of(boardList));
        Mockito.when(boardListRepository.save(updatedBoardList)).thenReturn(updatedBoardList);

        // then
        BoardList actualBoard = boardListService.updateBoardList(boardList.boardListId(), updateDto);

        assertEquals(updatedBoardList, actualBoard);

        Mockito.verify(boardListRepository).findById(boardList.boardListId());
        Mockito.verify(boardListRepository).save(updatedBoardList);

    }

    @Test
    void updateBoard_shouldThrowIdNotFoundException_whenBoardNotFound() {

        //when
        Mockito.when(boardListRepository.findById(boardList.boardListId())).thenReturn(Optional.empty());

        //then
        assertThrows(IdNotFoundException.class, () -> boardListService.updateBoardList(boardList.boardListId(), boardListDto));

        Mockito.verify(boardListRepository).findById(boardList.boardListId());
    }


    @Test
    void deleteBoardListById_shouldDeleteBoard_whenCalledWithValidId() throws IdNotFoundException {
        Mockito.when(boardListRepository.existsById(boardList.boardListId())).thenReturn(true);
        boolean actualBoardList = boardListService.deleteBoardListById(boardList.boardListId());

        // then
        assertTrue(actualBoardList);

        Mockito.verify(boardListRepository).existsById(boardList.boardListId());
        Mockito.verify(boardListRepository).deleteById(boardList.boardListId());

    }

    @Test
    void deleteBoardListById_shouldThrowIdNotFoundException_whenCalledWithInvalidId() throws IdNotFoundException {
        String notFoundBoardListId = "missing-123";
        //when
        Mockito.when(boardListRepository.existsById(notFoundBoardListId)).thenReturn(false);

        //then
        assertThrows(IdNotFoundException.class, () -> boardListService.deleteBoardListById(notFoundBoardListId));

        Mockito.verify(boardListRepository).existsById(notFoundBoardListId);
    }

}