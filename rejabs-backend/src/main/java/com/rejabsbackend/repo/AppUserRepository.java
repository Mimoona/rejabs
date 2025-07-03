package com.rejabsbackend.repo;

import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.model.BoardList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);
}
