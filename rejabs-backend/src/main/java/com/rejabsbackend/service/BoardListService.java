package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardListDto;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.BoardList;
import com.rejabsbackend.repo.BoardListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardListService {
    private final BoardListRepository boardListRepository;
    private final IdService idService;

    public BoardListService(BoardListRepository boardListRepository, IdService idService) {
        this.boardListRepository = boardListRepository;
        this.idService = idService;
    }

    public List<BoardList> getAllListsByBoardId() {
        return boardListRepository.findAll();
    }

    public BoardList getBoardListById(String boardListId) throws IdNotFoundException {
        return boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IdNotFoundException(boardListId, "Board List"));
    }

    public BoardList createBoardList(BoardListDto boardListDto) {
        BoardList boardList = new BoardList(
                idService.generateId(),
                boardListDto.listTitle(),
                boardListDto.boardId(),
                boardListDto.position()
        );

        return boardListRepository.save(boardList);
    }

    public BoardList updateBoardList(String boardListId, BoardListDto boardListDto) throws IdNotFoundException {

        BoardList boardListExist = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IdNotFoundException(boardListId, "Board List"));

        BoardList updatedBoardList =  new BoardList(
                boardListExist.boardListId(),
                boardListDto.listTitle(),
                boardListExist.boardId(),
                boardListDto.position()
        );
        return boardListRepository.save(updatedBoardList);
    }

    public boolean deleteBoardListById(String boardListId) {
        if(boardListRepository.existsById(boardListId)) {
            boardListRepository.deleteById(boardListId);
            return true;
        }
        return false;
    }
}
