package com.rejabsbackend.dto;

import java.util.List;
public record BoardDto(
        String title,
        List<String> collaborators) {
}