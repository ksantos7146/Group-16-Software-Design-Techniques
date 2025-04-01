package com.example.softwaredesigntechniques.dto.http.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotFoundErrorResponseDto {
    private String message;
}
