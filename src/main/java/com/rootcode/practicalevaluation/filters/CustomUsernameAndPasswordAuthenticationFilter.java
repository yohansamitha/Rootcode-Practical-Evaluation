package com.rootcode.practicalevaluation.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rootcode.practicalevaluation.dto.tokens.TokenDTO;
import com.rootcode.practicalevaluation.dto.user.LoginDTO;
import com.rootcode.practicalevaluation.utils.functions.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String TOKEN = "jwtToken";
    private static final String USERID = "userId";
    private static final String USEREMAIL = "userEmail";

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public CustomUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            LoginDTO authenticationRequest = this.objectMapper.readValue(request.getInputStream(), LoginDTO.class);

            try {
                // Proceed with authentication
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        new String(decoder.decode(authenticationRequest.getPassword()))
                );

                return authenticationManager.authenticate(authentication);
            } catch (BadCredentialsException bce) {
                log.error("Invalid credentials: ", bce);
                throw bce;
            } catch (DisabledException de) {
                log.error("User account is disabled: ", de);
                throw new BadCredentialsException("User account is disabled.");
            } catch (LockedException le) {
                log.error("User account is locked: ", le);
                throw new BadCredentialsException("User account is locked.");
            } catch (AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                throw ex;
            }
        } catch (IOException e) {
            log.error("Error reading authentication request: ", e);
            throw new RuntimeException("Failed to parse authentication request.", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        try {
            // Create JWT Token
            Date loggingDate = new Date();
            TokenDTO tokenDTO = jwtUtil.getTokensDTO(authResult.getName(), loggingDate);

            // Prepare the response object
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("code", 200);
            responseObject.put(TOKEN, tokenDTO.getToken());
            responseObject.put(USERID, tokenDTO.getUserID());
            responseObject.put(USEREMAIL, tokenDTO.getUserEmail());

            // Write the response
            writeJsonResponse(response, responseObject, HttpServletResponse.SC_OK);

        } catch (Exception e) {
            log.error("Error during successful authentication: ", e);
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Invalid username or password.");
        error.put("code", HttpServletResponse.SC_UNAUTHORIZED);

        // Write the error response
        writeJsonResponse(response, error, HttpServletResponse.SC_UNAUTHORIZED);
        SecurityContextHolder.clearContext();
    }

    // Utility method to write JSON responses
    private void writeJsonResponse(HttpServletResponse response, Object responseObject, int status) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(responseObject));
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
}
