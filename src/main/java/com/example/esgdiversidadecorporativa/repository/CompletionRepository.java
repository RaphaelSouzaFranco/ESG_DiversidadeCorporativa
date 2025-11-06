package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Completion;
import com.example.esgdiversidadecorporativa.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, String> {

    // 🔹 Busca uma conclusão específica pela matrícula
    Optional<Completion> findByEnrollment(Enrollment enrollment);

    // 🔹 Lista todas as conclusões de uma matrícula
    List<Completion> findAllByEnrollment(Enrollment enrollment);

    // 🔹 Busca todas as conclusões com resultado "Aprovado" ou "Completo"
    @Query("SELECT c FROM Completion c WHERE UPPER(c.result) IN ('APROVADO', 'COMPLETO')")
    List<Completion> findApprovedCompletions();

    // 🔹 Busca todas as conclusões com resultado "Reprovado" ou "Incompleto"
    @Query("SELECT c FROM Completion c WHERE UPPER(c.result) IN ('REPROVADO', 'INCOMPLETO')")
    List<Completion> findFailedCompletions();

    // 🔹 Busca conclusões com resultado específico (ex: “APROVADO”)
    List<Completion> findByResultIgnoreCase(String result);
}
