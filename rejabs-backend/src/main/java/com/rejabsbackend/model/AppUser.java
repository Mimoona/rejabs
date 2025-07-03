package com.rejabsbackend.model;

import com.rejabsbackend.enums.AuthProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public record AppUser(
        @Id
        String id,                      // UUID or GitHub ID
        String username,               // Name
        String email,                  // Required, unique
        String password,               // null for GitHub users or set
        String avatarUrl,              // From GitHub or null
        AuthProvider provider          // LOCAL or GITHUB
) {
}
