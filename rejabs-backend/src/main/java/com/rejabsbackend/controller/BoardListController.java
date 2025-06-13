package com.rejabsbackend.controller;

import com.rejabsbackend.dto.BoardListDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.BoardList;
import com.rejabsbackend.service.BoardListService;
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
    public List<BoardList> getListsByBoardId() {
        return boardListService.getAllListsByBoardId();
    }

    @GetMapping("/{boardListId}")
    public BoardList getBoardListById(@PathVariable String boardListId) throws IdNotFoundException {
        return boardListService.getBoardListById(boardListId);
    }

    @PostMapping("/create")
    public BoardList createBoard(@RequestBody BoardListDto boardListDto) {
        return boardListService.createBoard(boardListDto);
    }

    @PutMapping("/{boardListId}")
    public BoardList updateBoard(@PathVariable String boardListId, @RequestBody BoardListDto boardListDTo) throws IdNotFoundException {
        return boardListService.updateBoard(boardListId, boardListDTo);
    }


    @DeleteMapping("/{boardListId}")
    public boolean deleteBoard(@PathVariable String boardListId) {
        return boardListService.deleteBoardById(boardListId);
    }
}
