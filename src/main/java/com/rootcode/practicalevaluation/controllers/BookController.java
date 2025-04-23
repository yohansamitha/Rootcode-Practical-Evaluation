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

    @GetMapping("/available")
    public ResponseEntity<StandardResponse> getAvailableBooks() {
        StandardResponse response = bookService.getAvailableBooks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<StandardResponse> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(bookService.searchAvailableBooks(author, year));
    }

}
