package com.rejabsbackend.service;

import com.rejabsbackend.dto.BoardDto;
import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.exception.IdNotFoundException;
import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.Collaborator;
import org.springframework.stereotype.Service;
import com.rejabsbackend.repo.BoardRepository;

import java.util.ArrayList;
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
        String userId = authService.getCurrentUserId();
        return boardRepository.findAllByOwnerId(userId);
    }

    public Board getBoardById(String boardId) throws IdNotFoundException {
        return boardRepository.findById(boardId).orElseThrow(() -> new IdNotFoundException(boardId, "Board"));
    }

    public Board createBoard(BoardDto boardDto) {

        List<Collaborator> collaborators = checkCollaborators(boardDto.collaborators());
        Board newBoard = new Board(idService.generateId(),
                boardDto.title(),
                authService.getCurrentUserId(),
                collaborators);
        boardRepository.save(newBoard);
        return newBoard;

    }

    public Board updateBoard(String boardId, BoardDto boardDto) throws IdNotFoundException {
        Board existingBoard = getBoardIfOwner(boardId);

        // Convert and validate collaborators
        List<Collaborator> updatedCollaborators = boardDto.collaborators() != null ? boardDto.collaborators() : existingBoard.collaborators();

        Board updatedBoard = new Board(
                existingBoard.boardId(),
                boardDto.title() != null ? boardDto.title() : existingBoard.title(),
                existingBoard.ownerId(),
                updatedCollaborators
        );
        return boardRepository.save(updatedBoard);
    }

    public boolean deleteBoardById(String boardId) throws IdNotFoundException {
        Board existingBoard = getBoardIfOwner(boardId);
        if (existingBoard != null) {
            boardRepository.delete(existingBoard);
            return true;
        }

        return false;
    }

    public Board getBoardIfOwner(String boardId) throws IdNotFoundException, AuthenticationException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IdNotFoundException(boardId, "Board"));

        String currentUserId = authService.getCurrentUserId();

        if (!board.ownerId().equals(currentUserId)) {
            throw new AuthenticationException("User is not the owner of the board");
        }

        return board;
    }

    private List<Collaborator> checkCollaborators(List<Collaborator> collaboratorsList) {
        if (collaboratorsList == null) {
            return new ArrayList<>();
        }

        return collaboratorsList.stream()
                .map(collaborator -> new Collaborator(
                        idService.generateId(),
                        collaborator.collaboratorName(),
                        collaborator.collaboratorEmail(),
                        collaborator.collaboratorAvatar()

                ))
                .toList();
    }
}

