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
    @Column(name = "report_id")
    private String reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_department_id", foreignKey = @ForeignKey(name = "diversity_department_FK"))
    @NotNull(message = "Departamento é obrigatório")
    private Department department;


    @Min(value = 0, message = "Total de funcionários não pode ser negativo")
    @Column(name = "total_employees", nullable = false)
    private Long totalEmployees = 0L;

    @Min(value = 0, message = "Total de homens não pode ser negativo")
    @Column(name = "total_male", nullable = false)
    private Long maleCount = 0L;

    @Min(value = 0, message = "Total de mulheres não pode ser negativo")
    @Column(name = "total_female", nullable = false)
    private Long femaleCount = 0L;

    @Min(value = 0, message = "Total de outros não pode ser negativo")
    @Column(name = "total_other", nullable = false)
    private Long otherCount = 0L;

    @Min(value = 0, message = "Total não informado não pode ser negativo")
    @Column(name = "total_not_informed", nullable = false)
    private Integer totalNotInformed = 0;

    @Column(name = "percentage_male")
    private Double percentageMale = 0.0;

    @Column(name = "percentage_female")
    private Double percentageFemale = 0.0;

    @Column(name = "percentage_other")
    private Double percentageOther = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Calcula automaticamente os percentuais ao criar
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculatePercentages();
    }

    //  para calcular percentuais
    public void calculatePercentages() {
        if (totalEmployees > 0) {
            percentageMale = (maleCount * 100.0) / totalEmployees;
            percentageFemale = (femaleCount * 100.0) / totalEmployees;
            percentageOther = (otherCount * 100.0) / totalEmployees;
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