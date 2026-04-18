package com.example.esgdiversidadecorporativa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "employee_id")
    private String employeeId;

    @NotBlank(message = "Nome do funcionÃ¡rio Ã© obrigatÃ³rio")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email Ã© obrigatÃ³rio")
    @Email(message = "Email deve ser vÃ¡lido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // diversidade (opcional)
    @Column(name = "gender", length = 20)
    private String gender; // "Masc", "Femin", "outros", "Prefiro nÃ£o informar"

    // muitos funcionÃ¡rios em um departamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_department_id",
            foreignKey = @ForeignKey(name = "employee_department_FK"))
    @NotNull(message = "Departamento Ã© obrigatÃ³rio")
    private Department department;

    // Relacionamento: Um funcionÃ¡rio tem muitas matrÃ­culas
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    //  auxiliar para adicionar matrÃ­cula
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setEmployee(this);
    }

    //  auxiliar para remover matrÃ­cula
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setEmployee(null);
    }
}