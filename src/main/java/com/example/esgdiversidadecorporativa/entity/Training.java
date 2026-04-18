package com.example.esgdiversidadecorporativa.entity;

import jakarta.persistence.*;
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
    @Column(name = "training_id", length = 100)
    private String trainingId;

    @NotBlank(message = "TÃ­tulo do treinamento Ã© obrigatÃ³rio")
    @Size(min = 5, max = 200, message = "TÃ­tulo deve ter entre 5 e 200 caracteres")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 500, message = "DescriÃ§Ã£o deve ter no mÃ¡ximo 500 caracteres")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Data de conclusÃ£o Ã© obrigatÃ³ria")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    // MÃƒÂ©todo auxiliar
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setTraining(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setTraining(null);
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }
}
