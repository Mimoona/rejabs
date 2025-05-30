package com.rejabsbackend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record AppUser(
        Integer id,
        String login,
        String email,
        String avatar_url
) {
}
