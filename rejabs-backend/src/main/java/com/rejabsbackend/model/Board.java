package com.rejabsbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "boards")

public record Board(
        @Id
        String boardId,
        String title,
        String ownerId,
        List<String> collaborators) {
}