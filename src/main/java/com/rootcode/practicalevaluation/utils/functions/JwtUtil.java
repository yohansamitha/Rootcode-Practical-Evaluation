package com.rootcode.practicalevaluation.utils.functions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rootcode.practicalevaluation.dto.tokens.TokenDTO;
import com.rootcode.practicalevaluation.dto.tokens.TokenPayLoadDTO;
import com.rootcode.practicalevaluation.repository.UserRepository;
import com.rootcode.practicalevaluation.utils.constant.JWTConfig;
import com.rootcode.practicalevaluation.utils.mapping.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final JWTConfig jwtConfig;
    private final UserRepository userRepository;

    @Autowired
    public JwtUtil(JWTConfig jwtConfig, UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }

    public String getUsernameFromToken(String token) {
        if (token != null) {
            String payLoad = JWT.require(
                            Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()))
                    .build().verify(token).getSubject();
            Date expiresAt = JWT.require(
                            Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()))
                    .build().verify(token).getExpiresAt();
            if (payLoad != null && !payLoad.isEmpty()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                TokenPayLoadDTO payLoadBean = gson.fromJson(payLoad, TokenPayLoadDTO.class);
                payLoadBean.setExpiresTime(expiresAt);
                return payLoadBean.getUserName();
            }
        }
        return null;
    }

    public TokenDTO getTokensDTO(String username, Date loggingTime) {
        TokenDTO refData = new TokenDTO();
        try {
            User optionalUser = userRepository.findByEmail(username).orElse(null);
            if (optionalUser == null) {
                // Log user not found
                log.error("User not found with username: {}", username);
                throw new IllegalArgumentException("User not found");
            }
            Date tokenExpireDate = new Date(loggingTime.getTime() + jwtConfig.getTokenExp());
            refData.setToken(this.getToken(username, loggingTime, tokenExpireDate));
            refData.setUserEmail(optionalUser.getEmail());
            refData.setUserID(String.valueOf(optionalUser.getUserId()));
            return refData;
        } catch (Exception ex) {
            log.error("Exception : ", ex);
            throw ex;
        }
    }

    private String getToken(String username, Date loggingTime, Date tokenExpireDate) {
        try {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            TokenPayLoadDTO payLoadBean = new TokenPayLoadDTO(sessionId, username, tokenExpireDate);

            Gson gson = new Gson();
            String payLoadJson = gson.toJson(payLoadBean);

            String token = JWT.create()
                    .withSubject(payLoadJson)
                    .withIssuedAt(loggingTime)
                    .withIssuer("Root Code")
                    .withExpiresAt(tokenExpireDate)
                    .sign(Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()));

            token = jwtConfig.getTokenPrefix() + token;
            return token;
        } catch (Exception ex) {
            log.error("Exception : ", ex);
            throw ex;
        }
    }
}
