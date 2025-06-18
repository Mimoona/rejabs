package com.rejabsbackend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Document(collection = "cards")
public record Card(
        @Id
        String cardId,
        String cardTitle,
        String description,
        String listId,
        int position,
        List<String> labels,
        Date dueDate
) {
}
