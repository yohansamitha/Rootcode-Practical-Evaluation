package com.rootcode.practicalevaluation.utils.mapping;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Many borrow records can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many borrow records can be related to one book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime borrowedAt = LocalDateTime.now();

    private LocalDateTime returnedAt;

    // Relationship Purpose:
    // - Acts as a transaction record between a User and a Book.
    // - Enables borrowing history tracking and return handling.
}