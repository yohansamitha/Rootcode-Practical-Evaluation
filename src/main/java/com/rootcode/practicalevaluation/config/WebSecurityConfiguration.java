package com.rootcode.practicalevaluation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rootcode.practicalevaluation.filters.CustomUsernameAndPasswordAuthenticationFilter;
import com.rootcode.practicalevaluation.filters.JwtFilter;
import com.rootcode.practicalevaluation.services.user.UserService;
import com.rootcode.practicalevaluation.utils.constant.JWTConfig;
import com.rootcode.practicalevaluation.utils.constant.SecurityConfigurationProperties;
import com.rootcode.practicalevaluation.utils.functions.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {"/login/**",
            "/api/users/register",
            "/api/books",
            "/api/books/**"};

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityConfigurationProperties configurationProperties;
    @Autowired
    @Lazy
    private UserService userDetailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JWTConfig jwtConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exp -> exp.authenticationEntryPoint(restAuthenticationEntryPoint()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .addFilter(new CustomUsernameAndPasswordAuthenticationFilter(authenticationManager, jwtUtil, objectMapper))
                .addFilterBefore(new JwtFilter(authenticationManager, userDetailService, jwtConfig, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("CORS Configuration : {}", configurationProperties.toString());
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(configurationProperties.getAllowedOrigins());
        configuration.setAllowedMethods(configurationProperties.getAllowedMethods());
        configuration.setAllowedHeaders(configurationProperties.getAllowedHeaders());
        configuration.setAllowCredentials(configurationProperties.isAllowedCredentials());
        configuration.setExposedHeaders(configurationProperties.getExposedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        log.info("Logout Success Handler : {}", HttpStatus.OK);
        return new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK);
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, e) -> {
            Map<String, Object> errorObject = new HashMap<>();
            errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("code", 401);

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(errorObject));
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
