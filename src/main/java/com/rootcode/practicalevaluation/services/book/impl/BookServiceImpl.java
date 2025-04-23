package com.rootcode.practicalevaluation.services.book.impl;

import com.rootcode.practicalevaluation.dto.book.AvailableBookDTO;
import com.rootcode.practicalevaluation.dto.book.BookDTO;
import com.rootcode.practicalevaluation.dto.responses.KeyValueDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.exceptions.BadRequestException;
import com.rootcode.practicalevaluation.exceptions.ResourceNotFoundException;
import com.rootcode.practicalevaluation.repository.BookRepository;
import com.rootcode.practicalevaluation.services.book.BookService;
import com.rootcode.practicalevaluation.utils.mapping.Book;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public StandardResponse getAvailableBooks() {
        List<AvailableBookDTO> books = bookRepository.findAvailableBooksDTO();
        return new StandardResponse("200", "Available books retrieved successfully",
                Collections.singletonList(new KeyValueDTO("books", books)));
    }

    @Override
    @Cacheable(value = "bookSearch", key = "#author + '_' + #year")
    public StandardResponse searchAvailableBooks(String author, Integer year) {
        System.out.println("Fetching from DB...");
        List<AvailableBookDTO> books = bookRepository.searchAvailableBooks(author, year);
        return new StandardResponse("200", "Filtered books retrieved successfully",
                Collections.singletonList(new KeyValueDTO("books", books)));
    }

    @Override
    public StandardResponse insert(@Valid BookDTO dto) {
        Book book = mapper.map(dto, Book.class);
        bookRepository.save(book);
        return new StandardResponse("201", "Book inserted successfully", null);
    }

    @Override
    public StandardResponse update(@Valid BookDTO dto) {
        if (dto.getId() == null || dto.getId() <= 0) {
            throw new BadRequestException("Invalid book ID provided");
        }
        Book book = bookRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + dto.getId()));
        mapper.map(dto, book);
        bookRepository.save(book);
        return new StandardResponse("200", "Book updated successfully", null);
    }

    @Override
    public StandardResponse findById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        BookDTO bookDTO = mapper.map(book, BookDTO.class);
        return new StandardResponse("200", "Book retrieved successfully", Collections.singletonList(new KeyValueDTO("book", bookDTO)));
    }

    @Override
    public StandardResponse deleteById(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
        return new StandardResponse("200", "Book deleted successfully", null);
    }
}
