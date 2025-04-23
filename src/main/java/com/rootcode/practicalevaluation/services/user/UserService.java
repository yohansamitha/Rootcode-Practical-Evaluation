package com.rootcode.practicalevaluation.services.user;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.dto.user.UserDTO;
import com.rootcode.practicalevaluation.services.CrudService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, CrudService<UserDTO, Integer> {
    StandardResponse getBorrowingHistory(String userEmail);
}
