package com.rootcode.practicalevaluation.dto.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String token;
    private String userID;
    private String userEmail;
}
