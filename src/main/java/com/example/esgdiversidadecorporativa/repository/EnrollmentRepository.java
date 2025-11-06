package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Enrollment;
import com.example.esgdiversidadecorporativa.model.Employee;
import com.example.esgdiversidadecorporativa.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    // Busca inscrições de um funcionário
    List<Enrollment> findByEmployee(Employee employee);

    // Busca inscrições de um treinamento
    List<Enrollment> findByTraining(Training training);

    // Verifica se o funcionário já está inscrito em determinado treinamento
    boolean existsByEmployeeAndTraining(Employee employee, Training training);

    // Busca inscrição específica por funcionário e treinamento
    Optional<Enrollment> findByEmployeeAndTraining(Employee employee, Training training);
}
