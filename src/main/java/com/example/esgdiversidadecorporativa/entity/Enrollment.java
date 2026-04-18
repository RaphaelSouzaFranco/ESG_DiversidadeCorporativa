package com.example.esgdiversidadecorporativa.entity;

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

    @NotNull(message = "Data de matrÃ­cula Ã© obrigatÃ³ria")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "last_notification_date")
    private LocalDate lastNotificationDate;

    // Muitas matrÃƒÂ­culas pertencem a um funcionÃƒÂ¡rio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_employee_id",
            foreignKey = @ForeignKey(name = "enrollment_employee_FK"))
    @NotNull(message = "FuncionÃ¡rio Ã© obrigatÃ³rio")
    private Employee employee;

    // Muitas matrÃƒÂ­culas pertencem a um treinamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_training_id",
            foreignKey = @ForeignKey(name = "enrollment_training_FK"))
    @NotNull(message = "Treinamento Ã© obrigatÃ³rio")
    private Training training;

    // Uma matrÃ­cula pode ter uma conclusÃ£o (ou nenhuma)
    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Completion completion;

    //Define a conclusÃ£o e sincroniza os dois lados da relaÃ§Ã£o
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

    // MÃ©todo auxiliar: matrÃ­cula concluÃƒÂ­da?
    public boolean isCompleted() {
        return completion != null;
    }

    // MÃ©todo auxiliar: precisa de notificaÃƒÂ§ÃƒÂ£o?
    public boolean needsNotification() {
        if (isCompleted()) {
            return false;
        }
        if (lastNotificationDate == null) {
            return true;
        }
        return LocalDate.now().isAfter(lastNotificationDate.plusDays(7));
    }

    //  Gera ID automaticamente caso nÃƒÂ£o exista (UUID)
    @PrePersist
    public void generateId() {
        if (this.enrollmentId == null || this.enrollmentId.isBlank()) {
            this.enrollmentId = "ENR_" + UUID.randomUUID();
        }
    }
}
