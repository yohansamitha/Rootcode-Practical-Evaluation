package com.rootcode.practicalevaluation.services.borrow.impl;

import com.rootcode.practicalevaluation.dto.responses.KeyValueDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.exceptions.BadRequestException;
import com.rootcode.practicalevaluation.exceptions.ResourceNotFoundException;
import com.rootcode.practicalevaluation.repository.BookRepository;
import com.rootcode.practicalevaluation.repository.BorrowRecordRepository;
import com.rootcode.practicalevaluation.repository.UserRepository;
import com.rootcode.practicalevaluation.services.borrow.BorrowService;
import com.rootcode.practicalevaluation.utils.mapping.Book;
import com.rootcode.practicalevaluation.utils.mapping.BorrowRecord;
import com.rootcode.practicalevaluation.utils.mapping.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public StandardResponse borrowBook(Long bookId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid user. Please login again."));

        Book book = bookRepository.findById(Math.toIntExact(bookId))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("Book is out of stock.");
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setUser(user);
        borrowRecord.setBorrowedAt(LocalDateTime.now());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        borrowRepository.save(borrowRecord);

        return new StandardResponse("200", "Book borrowed successfully", List.of(
                new KeyValueDTO("bookTitle", book.getTitle()),
                new KeyValueDTO("borrowedAt", borrowRecord.getBorrowedAt().toString())
        ));
    }

    @Override
    public StandardResponse returnBook(Long borrowId, String email) {
        BorrowRecord borrowRecord = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found."));

        if (!borrowRecord.getUser().getEmail().equals(email)) {
            throw new BadRequestException("You are not authorized to return this book.");
        }

        borrowRecord.setReturnedAt(LocalDateTime.now());

        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
        borrowRepository.save(borrowRecord);

        return new StandardResponse("200", "Book returned successfully", List.of(
                new KeyValueDTO("bookTitle", book.getTitle()),
                new KeyValueDTO("returnedAt", borrowRecord.getReturnedAt().toString())
        ));
    }
}
