package com.rootcode.practicalevaluation.services.user;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.dto.user.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    StandardResponse insert(UserDTO t);

    StandardResponse update(UserDTO u);

    StandardResponse findById(Long id);

    StandardResponse deleteById(Long id);

    StandardResponse getBorrowingHistory(String userEmail);
}
