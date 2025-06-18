package com.rejabsbackend.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record CardDto(String cardTitle,
                      String description,
                      String listId,
                      int position,
                      List<String> labels,
                      Date dueDate) {

}
