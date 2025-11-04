package com.example.esgdiversidadecorporativa.model;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
    @Column(name = "employee_id")
    private Long employeeId;

    @NotBlank(message = "Nome do funcionário é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // diversidade (opcional)
    @Column(name = "gender", length = 20)
    private String gender; // "Masc", "Femin", "outros", "Prefiro não informar"

    // muitos funcionários em um departamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_department_id",
            foreignKey = @ForeignKey(name = "employee_department_FK"))
    @NotNull(message = "Departamento é obrigatório")
    private Department department;

    // Relacionamento: Um funcionário tem muitas matrículas
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    //  auxiliar para adicionar matrícula
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setEmployee(this);
    }

    //  auxiliar para remover matrícula
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setEmployee(null);
    }
}