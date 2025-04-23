package com.rootcode.practicalevaluation.services.borrow;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import jakarta.validation.constraints.Min;

public interface BorrowService {
    StandardResponse borrowBook(@Min(1) Long bookId, String email);

    StandardResponse returnBook(@Min(1) Long borrowId, String email);
}
