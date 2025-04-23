package com.rootcode.practicalevaluation.dto.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayLoadDTO {
    private String sessionId;
    private String userName;
    private Date expiresTime;
}
