package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "diversity_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diversity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diversity_report_seq")
    @SequenceGenerator(name = "diversity_report_seq", sequenceName = "diversity_report_seq", allocationSize = 1)
    @Column(name = "report_id")
    private Long reportId;

    @NotNull(message = "ID do departamento é obrigatório")
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @NotBlank(message = "Nome do departamento é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Min(value = 0, message = "Total de funcionários não pode ser negativo")
    @Column(name = "total_employees", nullable = false)
    private Integer totalEmployees;

    @Min(value = 0, message = "Total de homens não pode ser negativo")
    @Column(name = "total_male", nullable = false)
    private Integer totalMale;

    @Min(value = 0, message = "Total de mulheres não pode ser negativo")
    @Column(name = "total_female", nullable = false)
    private Integer totalFemale;

    @Min(value = 0, message = "Total de outros não pode ser negativo")
    @Column(name = "total_other", nullable = false)
    private Integer totalOther;

    @Min(value = 0, message = "Total não informado não pode ser negativo")
    @Column(name = "total_not_informed", nullable = false)
    private Integer totalNotInformed;

    @Column(name = "percentage_male")
    private Double percentageMale;

    @Column(name = "percentage_female")
    private Double percentageFemale;

    @Column(name = "percentage_other")
    private Double percentageOther;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //  executado antes de persistir a entidade
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculatePercentages();
    }

    //  para calcular percentuais
    public void calculatePercentages() {
        if (totalEmployees > 0) {
            percentageMale = (totalMale * 100.0) / totalEmployees;
            percentageFemale = (totalFemale * 100.0) / totalEmployees;
            percentageOther = (totalOther * 100.0) / totalEmployees;
        } else {
            percentageMale = 0.0;
            percentageFemale = 0.0;
            percentageOther = 0.0;
        }
    }

    //  auxiliar para verificar se tem boa diversidade (exemplo: 30% de cada gênero)
    public boolean hasGoodDiversity() {
        return percentageMale >= 30.0 &&
                percentageFemale >= 30.0;
    }
}