package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollment_seq")
    @SequenceGenerator(name = "enrollment_seq", sequenceName = "enrollment_seq", allocationSize = 1)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @NotNull(message = "Data de matrícula é obrigatória")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "last_notification_date")
    private LocalDate lastNotificationDate;

    //  Muitas matrículas são de  um funcionário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_employee_id",
            foreignKey = @ForeignKey(name = "enrollment_employee_FK"))
    @NotNull(message = "Funcionário é obrigatório")
    private Employee employee;

    // Muitas matrículas são de um treinamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_training_id",
            foreignKey = @ForeignKey(name = "enrollment_training_FK"))
    @NotNull(message = "Treinamento é obrigatório")
    private Training training;

    // uma matrícula pode ter uma conclusão (ou nenhuma)
    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Completion completion;

    //  definir a conclusão
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

    //  auxiliar para verificar se está concluído
    public boolean isCompleted() {
        return completion != null;
    }

    // auxiliar para verificar se precisa de notificação
    public boolean needsNotification() {
        if (isCompleted()) {
            return false;
        }
        if (lastNotificationDate == null) {
            return true;
        }
        // Verifica se passaram 7 dias desde a última notificação
        return LocalDate.now().isAfter(lastNotificationDate.plusDays(7));
    }
}