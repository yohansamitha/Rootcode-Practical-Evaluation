package com.rootcode.practicalevaluation.services.book;

import com.rootcode.practicalevaluation.dto.book.BookDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.services.CrudService;

public interface BookService extends CrudService<BookDTO, Integer> {
    StandardResponse getAvailableBooks();

    StandardResponse searchAvailableBooks(String author, Integer year);
}
