package com.example.esgdiversidadecorporativa.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String recipient;
    private String subject;
    private String body;
}
