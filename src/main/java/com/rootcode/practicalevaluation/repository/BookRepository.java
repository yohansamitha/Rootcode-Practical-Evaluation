package com.rootcode.practicalevaluation.repository;

import com.rootcode.practicalevaluation.dto.book.AvailableBookDTO;
import com.rootcode.practicalevaluation.utils.mapping.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT new com.rootcode.practicalevaluation.dto.book.AvailableBookDTO(" +
            "b.id, b.title, b.author, b.publishedYear, b.availableCopies) " +
            "FROM Book b WHERE b.availableCopies > 0")
    List<AvailableBookDTO> findAvailableBooksDTO();

    @Query("SELECT new com.rootcode.practicalevaluation.dto.book.AvailableBookDTO(" +
            "b.id, b.title, b.author, b.publishedYear, b.availableCopies) " +
            "FROM Book b " +
            "WHERE (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:year IS NULL OR b.publishedYear = :year) " +
            "AND b.availableCopies > 0")
    List<AvailableBookDTO> searchAvailableBooks(String author, Integer year);

}
