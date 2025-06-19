package com.rejabsbackend.dto;

import com.rejabsbackend.enums.Label;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record CardDto(String cardTitle,
                      String description,
                      String listId,
                      int position,
                      List<Label> labels,
                      LocalDate dueDate) {

}
