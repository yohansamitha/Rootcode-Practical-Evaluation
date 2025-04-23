package com.rootcode.practicalevaluation.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueDTO {
    private String key;
    private Object value;
}
