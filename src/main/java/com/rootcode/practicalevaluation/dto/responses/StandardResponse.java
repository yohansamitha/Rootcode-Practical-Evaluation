package com.rootcode.practicalevaluation.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse {
    private String code;
    private String message;
    private List<KeyValueDTO> data;
}
