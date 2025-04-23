package com.rootcode.practicalevaluation.services.user.impl;

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
    public StandardResponse insert(UserDTO userDTO) {
        StandardResponse standardResponse = new StandardResponse();
        return standardResponse;
    }

    @Override
    public StandardResponse update(UserDTO userDTO) {
        StandardResponse standardResponse = new StandardResponse();
        return standardResponse;
    }

    @Override
    public StandardResponse findById(Long id) {
        StandardResponse standardResponse = new StandardResponse();
        return standardResponse;
    }

    @Override
    public StandardResponse deleteById(Long aLong) {
        StandardResponse standardResponse = new StandardResponse();
        return standardResponse;
    }
}
