package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "completion", indexes = @Index(name = "completion_IDX", columnList = "enrollment_enrollment_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Completion {

    @Id
    @Column(name = "completion_id", nullable = false, length = 100)
    private String completionId = UUID.randomUUID().toString(); // Gera ID automaticamente

    @NotNull(message = "Data de conclusão é obrigatória")
    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    @NotBlank(message = "Resultado é obrigatório")
    @Size(max = 50, message = "Resultado deve ter no máximo 50 caracteres")
    @Column(name = "result", nullable = false, length = 50)
    private String result; // "completo", "incompleto", "aprovado", "reprovado"

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_enrollment_id",
            foreignKey = @ForeignKey(name = "completion_enrollment_FK"),
            unique = true)
    @NotNull(message = "Matrícula é obrigatória")
    private Enrollment enrollment;

    public boolean isApproved() {
        return "COMPLETO".equalsIgnoreCase(result) ||
                "APROVADO".equalsIgnoreCase(result);
    }

    public boolean isFailed() {
        return "INCOMPLETO".equalsIgnoreCase(result) ||
                "REPROVADO".equalsIgnoreCase(result);
    }
}
