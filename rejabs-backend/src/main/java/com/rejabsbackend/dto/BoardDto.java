package com.rejabsbackend.dto;

import java.util.List;
public record BoardDto(
        String title,
        String ownerId,
        List<String> collaborators) {

}