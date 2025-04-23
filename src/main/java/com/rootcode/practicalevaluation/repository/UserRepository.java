package com.rootcode.practicalevaluation.repository;

import com.rootcode.practicalevaluation.utils.mapping.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT U FROM User U WHERE U.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
