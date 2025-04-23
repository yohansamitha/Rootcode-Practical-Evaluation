package com.rootcode.practicalevaluation.controllers;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.services.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Single endpoint for both listing and searching books
    @GetMapping
    public ResponseEntity<StandardResponse> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer year) {

        // If filters are provided, call search method
        if (author != null || year != null) {
            return ResponseEntity.ok(bookService.searchAvailableBooks(author, year));
        }

        // Else, return all available books
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }
}
