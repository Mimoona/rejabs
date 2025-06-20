package com.rejabsbackend.repo;

import com.rejabsbackend.model.Board;
import com.rejabsbackend.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
}
