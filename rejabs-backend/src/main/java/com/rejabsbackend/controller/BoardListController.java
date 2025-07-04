package com.rejabsbackend.controller;

import com.rejabsbackend.dto.BoardListDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.BoardList;
import com.rejabsbackend.service.BoardListService;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-list")
public class BoardListController {

    private final BoardListService boardListService;

    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }

    @GetMapping
    public List<BoardList> getAllBoardLists() {
        return boardListService.getAllBoardLists();
    }

    @GetMapping("/{boardListId}")
    public BoardList getBoardListById(@PathVariable String boardListId) throws IdNotFoundException {
        return boardListService.getBoardListById(boardListId);
    }

    @PostMapping("/create")
    public BoardList createBoardList(@RequestBody BoardListDto boardListDto) {
        return boardListService.createBoardList(boardListDto);
    }

    @PutMapping("/{boardListId}")
    public BoardList updateBoardList(@PathVariable String boardListId, @RequestBody BoardListDto boardListDTo) throws IdNotFoundException {
        return boardListService.updateBoardList(boardListId, boardListDTo);
    }


    @DeleteMapping("/{boardListId}")
    public boolean deleteBoardList(@PathVariable String boardListId) throws IdNotFoundException {
        return boardListService.deleteBoardListById(boardListId);
    }
}
