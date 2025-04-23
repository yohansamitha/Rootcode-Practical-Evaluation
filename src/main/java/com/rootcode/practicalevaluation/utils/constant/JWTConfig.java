package com.rootcode.practicalevaluation.utils.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application.jwt")
public class JWTConfig {
    private String secretKey;
    private String tokenPrefix;
    private Long tokenExp;
}
