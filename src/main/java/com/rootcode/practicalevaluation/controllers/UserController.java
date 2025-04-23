package com.rootcode.practicalevaluation.controllers;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;
import com.rootcode.practicalevaluation.dto.user.UserDTO;
import com.rootcode.practicalevaluation.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/history")
    public ResponseEntity<StandardResponse> getOwnBorrowingHistory(Authentication authentication) {
        String userEmail = authentication.getName(); // Assuming email is the username
        StandardResponse response = userService.getBorrowingHistory(userEmail);
        return ResponseEntity.ok(response);
    }

    // Create a new user
    @PostMapping("/register")
    public ResponseEntity<StandardResponse> createUser(@Valid @RequestBody UserDTO userDTO) {
        StandardResponse response = userService.insert(userDTO);
        return ResponseEntity.ok(response);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getUserById(@PathVariable Integer id) {
        StandardResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    // Update a user
    @PutMapping()
    public ResponseEntity<StandardResponse> updateUser(@Valid @RequestBody UserDTO userDTO) {
        StandardResponse response = userService.update(userDTO);
        return ResponseEntity.ok(response);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteUser(@PathVariable Integer id) {
        StandardResponse response = userService.deleteById(id);
        return ResponseEntity.ok(response);
    }
}