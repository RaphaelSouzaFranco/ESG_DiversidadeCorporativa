package com.example.esgdiversidadecorporativa.dto;

import lombok.Data;

@Data
public class DiversityDto {
    private String reportId;
    private String departmentId;
    private Long totalEmployees;
    private Long maleCount;
    private Long femaleCount;
    private Long otherCount;
    private Double percentageMale;
    private Double percentageFemale;
    private Double percentageOther;
}
