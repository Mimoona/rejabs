package com.rejabsbackend.repo;

import com.rejabsbackend.model.BoardList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardListRepository extends MongoRepository<BoardList, String> {
    List<BoardList> findByBoardIdOrderByPositionAsc(String boardId);
}
