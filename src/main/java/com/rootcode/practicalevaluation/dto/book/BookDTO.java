package com.rootcode.practicalevaluation.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Integer id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotNull(message = "Published year is required")
    @Min(value = 1, message = "Published year must be greater than 0")
    private int publishedYear;

    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;

}
