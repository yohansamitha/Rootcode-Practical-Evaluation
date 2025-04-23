package com.rootcode.practicalevaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BorrowingHistoryDTO {
    private String bookTitle;
    private String author;
    private int publishedYear;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
}
