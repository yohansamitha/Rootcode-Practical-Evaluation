package com.rootcode.practicalevaluation.controllers;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.services.borrow.BorrowService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // ✅ Borrow a book
    @PostMapping("/{bookId}")
    public ResponseEntity<StandardResponse> borrowBook(@PathVariable @Min(1) Long bookId, Authentication authentication) {
        String email = authentication.getName(); // use email as username
        StandardResponse response = borrowService.borrowBook(bookId, email);
        return ResponseEntity.ok(response);
    }

    // ✅ Return a borrowed book
    @PostMapping("/return/{borrowId}")
    public ResponseEntity<StandardResponse> returnBook(@PathVariable @Min(1) Long borrowId, Authentication authentication) {
        String email = authentication.getName();
        StandardResponse response = borrowService.returnBook(borrowId, email);
        return ResponseEntity.ok(response);
    }

}