package com.rootcode.practicalevaluation.services.book;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;

public interface BookService {
    StandardResponse getAvailableBooks();

    StandardResponse searchAvailableBooks(String author, Integer year);
}
