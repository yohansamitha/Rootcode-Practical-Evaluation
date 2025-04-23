package com.rootcode.practicalevaluation.repository;

import com.rootcode.practicalevaluation.utils.mapping.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
}
