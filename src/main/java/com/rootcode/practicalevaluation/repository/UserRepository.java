package com.rootcode.practicalevaluation.repository;

import com.rootcode.practicalevaluation.dto.BorrowingHistoryDTO;
import com.rootcode.practicalevaluation.utils.mapping.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT U FROM User U WHERE U.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.rootcode.practicalevaluation.dto.BorrowingHistoryDTO(" +
            "b.book.title, b.book.author, b.book.publishedYear, b.borrowedAt, b.returnedAt) " +
            "FROM BorrowRecord b WHERE b.user.email = :email")
    List<BorrowingHistoryDTO> findHistoryByUserEmail(String email);

}
