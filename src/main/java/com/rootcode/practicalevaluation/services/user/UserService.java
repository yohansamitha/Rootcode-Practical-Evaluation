package com.rootcode.practicalevaluation.services.user;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.dto.user.UserDTO;
import com.rootcode.practicalevaluation.services.AbstractService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, AbstractService<UserDTO, Long> {
    StandardResponse getBorrowingHistory(String userEmail);
}
