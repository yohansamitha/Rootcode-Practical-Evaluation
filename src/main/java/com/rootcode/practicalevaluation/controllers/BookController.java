package com.rootcode.practicalevaluation.controllers;

import com.rootcode.practicalevaluation.dto.book.BookDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.services.book.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Single endpoint for both listing and searching books
    @GetMapping("/available")
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

    // Example endpoint for inserting a book
    @PostMapping
    public ResponseEntity<StandardResponse> insertBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.insert(bookDTO));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<StandardResponse> getBookById(@PathVariable Integer id) {
        StandardResponse response = bookService.findById(id);
        return ResponseEntity.ok(response);
    }

    // Example endpoint for updating a book
    @PutMapping
    public ResponseEntity<StandardResponse> updateBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.update(bookDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteBook(@PathVariable Integer id) {
        StandardResponse response = bookService.deleteById(id);
        return ResponseEntity.ok(response);
    }
}
