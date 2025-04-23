package com.rootcode.practicalevaluation.services.book.impl;

import com.rootcode.practicalevaluation.dto.AvailableBookDTO;
import com.rootcode.practicalevaluation.dto.responses.KeyValueDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.repository.BookRepository;
import com.rootcode.practicalevaluation.services.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public StandardResponse getAvailableBooks() {
        List<AvailableBookDTO> books = bookRepository.findAvailableBooksDTO();
        return new StandardResponse("200", "Available books retrieved successfully",
                Collections.singletonList(new KeyValueDTO("books", books)));
    }

    @Override
    public StandardResponse searchAvailableBooks(String author, Integer year) {
        List<AvailableBookDTO> books = bookRepository.searchAvailableBooks(author, year);
        return new StandardResponse("200", "Filtered books retrieved successfully",
                Collections.singletonList(new KeyValueDTO("books", books)));
    }
}
