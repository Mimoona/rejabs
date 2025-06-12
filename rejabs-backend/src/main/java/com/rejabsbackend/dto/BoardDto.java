package com.rejabsbackend.dto;

import com.rejabsbackend.model.Collaborator;

import java.util.List;
public record BoardDto(
        String title,
        List<Collaborator> collaborators) {

}