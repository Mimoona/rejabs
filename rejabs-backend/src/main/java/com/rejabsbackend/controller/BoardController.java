package com.rejabsbackend.controller;

import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }


    @GetMapping("/{boardId}")
    public Board getBoardById(@PathVariable String boardId) throws IdNotFoundException {
        return boardService.getBoardById(boardId);

    }

    @PostMapping
    public Board createBoard(@RequestBody BoardDto boardDto) {
        return boardService.createBoard(boardDto);
    }

    @PutMapping("/{boardId}")
    public Board updateBoard(@PathVariable String boardId, @RequestBody BoardDto boardDto) throws IdNotFoundException{
        return boardService.updateBoard(boardId, boardDto);
    }


    @DeleteMapping("/{boardId}")
    public boolean deleteBoard(@PathVariable String boardId) throws IdNotFoundException {
        return boardService.deleteBoardById(boardId);
    }
}
