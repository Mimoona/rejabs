package com.rejabsbackend.model;

import com.rejabsbackend.enums.Label;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;
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
        List<Label> labels,
        LocalDate dueDate,
        Instant createdAt,
        Instant updatedAt
) {
}
