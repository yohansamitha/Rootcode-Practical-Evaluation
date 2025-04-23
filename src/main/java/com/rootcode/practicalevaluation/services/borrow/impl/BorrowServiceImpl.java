package com.rootcode.practicalevaluation.services.borrow.impl;

import com.rootcode.practicalevaluation.dto.responses.KeyValueDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
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
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private BorrowRecordRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // Borrow a book
    @Override
    public StandardResponse borrowBook(Long bookId, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new StandardResponse("400", "Invalid user. Please login again.", null);
        }

        Optional<Book> optionalBook = bookRepository.findById(Math.toIntExact(bookId));
        if (optionalBook.isEmpty()) {
            return new StandardResponse("404", "Book not found.", null);
        }

        Book book = optionalBook.get();
        if (book.getAvailableCopies() <= 0) {
            return new StandardResponse("409", "Book is out of stock.", null);
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setUser(optionalUser.get());
        borrowRecord.setBorrowedAt(LocalDateTime.now());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        borrowRepository.save(borrowRecord);

        return new StandardResponse("200", "Book borrowed successfully", List.of(
                new KeyValueDTO("bookTitle", book.getTitle()),
                new KeyValueDTO("borrowedAt", borrowRecord.getBorrowedAt().toString())
        ));
    }


    // Return a book
    @Override
    public StandardResponse returnBook(Long borrowId, String email) {
        Optional<BorrowRecord> optionalBorrow = borrowRepository.findById(borrowId);
        if (optionalBorrow.isEmpty()) {
            return new StandardResponse("404", "Borrow record not found.", null);
        }

        BorrowRecord borrowRecord = optionalBorrow.get();

        if (!borrowRecord.getUser().getEmail().equals(email)) {
            return new StandardResponse("403", "You are not authorized to return this book.", null);
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
