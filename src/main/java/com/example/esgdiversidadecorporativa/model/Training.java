package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "training_seq", allocationSize = 1)
    @Column(name = "training_id")
    private Long trainingId;

    @NotBlank(message = "Título do treinamento é obrigatório")
    @Size(min = 5, max = 200, message = "Título deve ter entre 5 e 200 caracteres")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Descrição do treinamento é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @NotNull(message = "Data de conclusão é obrigatória")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // um treinamento tem várias matrículas
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    //  auxiliar para adicionar matrícula
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setTraining(this);
    }

    //  auxiliar para remover matrícula
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setTraining(null);
    }

    // auxiliar para verificar se está vencido
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }
}