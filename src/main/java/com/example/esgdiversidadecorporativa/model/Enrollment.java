package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @Column(name = "enrollment_id", length = 100)
    private String enrollmentId;

    @NotNull(message = "Data de matrícula é obrigatória")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "last_notification_date")
    private LocalDate lastNotificationDate;

    // 🔗 Muitas matrículas pertencem a um funcionário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_employee_id",
            foreignKey = @ForeignKey(name = "enrollment_employee_FK"))
    @NotNull(message = "Funcionário é obrigatório")
    private Employee employee;

    // 🔗 Muitas matrículas pertencem a um treinamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_training_id",
            foreignKey = @ForeignKey(name = "enrollment_training_FK"))
    @NotNull(message = "Treinamento é obrigatório")
    private Training training;

    // 🔗 Uma matrícula pode ter uma conclusão (ou nenhuma)
    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Completion completion;

    // 🔧 Define a conclusão e sincroniza os dois lados da relação
    public void setCompletion(Completion completion) {
        if (completion == null) {
            if (this.completion != null) {
                this.completion.setEnrollment(null);
            }
        } else {
            completion.setEnrollment(this);
        }
        this.completion = completion;
    }

    // 🧠 Método auxiliar: matrícula concluída?
    public boolean isCompleted() {
        return completion != null;
    }

    // 🕐 Método auxiliar: precisa de notificação?
    public boolean needsNotification() {
        if (isCompleted()) {
            return false;
        }
        if (lastNotificationDate == null) {
            return true;
        }
        return LocalDate.now().isAfter(lastNotificationDate.plusDays(7));
    }

    // 🆔 Gera ID automaticamente caso não exista (UUID)
    @PrePersist
    public void generateId() {
        if (this.enrollmentId == null || this.enrollmentId.isBlank()) {
            this.enrollmentId = "ENR_" + UUID.randomUUID();
        }
    }
}
