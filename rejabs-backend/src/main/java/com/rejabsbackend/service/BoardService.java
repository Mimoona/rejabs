package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.model.Board;
import org.springframework.stereotype.Service;
import com.rejabsbackend.repo.BoardRepository;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final IdService idService;
    private final AuthService authService;


    public BoardService(BoardRepository boardRepository, IdService idService, AuthService authService) {
        this.boardRepository = boardRepository;
        this.idService = idService;
        this.authService = authService;
    }


    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(String boardId) throws IdNotFoundException {
       return  boardRepository.findById(boardId).orElseThrow(()-> new IdNotFoundException(boardId, "Board"));
    }

    public Board createBoard(BoardDto boardDto) {
        Board newBoard = new Board(idService.generateId(),
                boardDto.title(),
                authService.getCurrentUserId(),
                boardDto.collaborators());
        boardRepository.save(newBoard);
        return newBoard;

    }

    public Board updateBoard(String boardId, BoardDto boardDto) throws IdNotFoundException {
        Board existingBoard = getBoardIfOwner(boardId);

        Board updatedBoard = new Board(
                existingBoard.boardId(),
                boardDto.title(),
                existingBoard.ownerId(),
                boardDto.collaborators()
        );
        return boardRepository.save(updatedBoard);
    }

    public boolean deleteBoardById(String boardId) throws IdNotFoundException{
        Board existingBoard = getBoardIfOwner(boardId);
        if(existingBoard != null){
            boardRepository.delete(existingBoard);
            return true;
        }

        return false;
    }

    public Board getBoardIfOwner(String boardId) throws IdNotFoundException, AuthenticationException{
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IdNotFoundException(boardId, "Board"));

        String currentUserId = authService.getCurrentUserId();

        if (!board.ownerId().equals(currentUserId)) {
            throw new AuthenticationException("User is not the owner of the board");
        }

        return board;
    }
}
