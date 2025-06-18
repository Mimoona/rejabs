package com.rejabsbackend.dto;

public record BoardListDto(
        String listTitle,
        String boardId,  // Reference to Board
        int position
) {
}
