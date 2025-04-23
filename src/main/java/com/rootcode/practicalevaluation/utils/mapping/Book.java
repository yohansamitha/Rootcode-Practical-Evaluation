package com.rootcode.practicalevaluation.utils.mapping;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private int publishedYear;

    private int availableCopies;

    // One book can be borrowed in many different borrow records
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowRecords;

    // Relationship Purpose:
    // - To track how many times and by whom a book has been borrowed.
    // - Useful for reporting and managing available stock.
}