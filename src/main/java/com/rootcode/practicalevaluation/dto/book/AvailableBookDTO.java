package com.rootcode.practicalevaluation.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableBookDTO {
    private int id;
    private String title;
    private String author;
    private int publishedYear;
    private int availableCopies;
}