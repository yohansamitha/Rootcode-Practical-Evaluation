package com.rootcode.practicalevaluation.filters;

import com.rootcode.practicalevaluation.services.user.UserService;
import com.rootcode.practicalevaluation.utils.constant.JWTConfig;
import com.rootcode.practicalevaluation.utils.functions.CommonFunctions;
import com.rootcode.practicalevaluation.utils.functions.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
public class JwtFilter extends BasicAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserService userDetailService;
    private final JWTConfig jwtConfig;

    public JwtFilter(AuthenticationManager authenticationManager, UserService userDetailService, JWTConfig jwtConfig, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userDetailService = userDetailService;
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Incoming Request: {} {}", request.getMethod(), request.getRequestURI());

        if (CommonFunctions.stringIsNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            log.warn("Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {

            String username = jwtUtil.getUsernameFromToken(token);

            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authenticated user: {} with roles: {}", username, authorities);
        } catch (Exception e) {
            // Log and respond with error message for invalid token
            log.error("Invalid token: ", e);
            SecurityContextHolder.clearContext();
            throw e;
        }
        filterChain.doFilter(request, response);
    }

}
