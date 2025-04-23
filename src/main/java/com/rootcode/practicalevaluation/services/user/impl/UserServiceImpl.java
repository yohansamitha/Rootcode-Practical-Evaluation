package com.rootcode.practicalevaluation.services.user.impl;

import com.rootcode.practicalevaluation.dto.BorrowingHistoryDTO;
import com.rootcode.practicalevaluation.dto.responses.KeyValueDTO;
import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.dto.user.UserDTO;
import com.rootcode.practicalevaluation.repository.UserRepository;
import com.rootcode.practicalevaluation.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    @Override
    public StandardResponse getBorrowingHistory(String email) {
        List<BorrowingHistoryDTO> history = userRepository.findHistoryByUserEmail(email);
        return new StandardResponse("success", "History fetched successfully", Collections.singletonList(new KeyValueDTO("savedUser", history)));
    }

    @Override
    public StandardResponse insert(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode password
        var userEntity = mapper.map(userDTO, com.rootcode.practicalevaluation.utils.mapping.User.class);
        userRepository.save(userEntity);
        return new StandardResponse("201", "User created successfully", null);
    }

    @Override
    public StandardResponse update(UserDTO userDTO) {
        var existingUser = userRepository.findById((long) userDTO.getUserId()).orElse(null);
        if (existingUser == null) {
            return new StandardResponse("404", "User not found", null);
        }
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(existingUser);
        return new StandardResponse("200", "User updated successfully", null);
    }

    @Override
    public StandardResponse findById(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new StandardResponse("404", "User not found", null);
        }
        user.setPassword(null);
        return new StandardResponse("200", "User retrieved successfully", Collections.singletonList(new KeyValueDTO("user", mapper.map(user, UserDTO.class))));
    }

    @Override
    public StandardResponse deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return new StandardResponse("200", "User deleted successfully", null);
    }
}
