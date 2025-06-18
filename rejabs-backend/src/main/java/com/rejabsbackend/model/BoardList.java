package com.rejabsbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lists")
public record BoardList(
        @Id
        String boardListId,
        String listTitle,
        String boardId,  // Reference to Board
        int position) {
}
