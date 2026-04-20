package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.entity.Completion;
import com.example.esgdiversidadecorporativa.entity.Enrollment;
import com.example.esgdiversidadecorporativa.repository.CompletionRepository;
import com.example.esgdiversidadecorporativa.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@SuppressWarnings("null")
public class CompletionService {

    private static final Set<String> VALID_RESULTS =
            Set.of("COMPLETO", "INCOMPLETO", "APROVADO", "REPROVADO");

    private final CompletionRepository completionRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public CompletionService(CompletionRepository completionRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.completionRepository = completionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Completion> findAll() {
        return completionRepository.findAll();
    }

    public Optional<Completion> findById(String id) {
        return completionRepository.findById(id);
    }

    public Completion createCompletion(Completion completion) {

        if (completion.getEnrollment() == null || completion.getEnrollment().getEnrollmentId() == null) {
            throw new IllegalArgumentException("Uma conclusão precisa estar associada a uma matrícula válida.");
        }

        Enrollment enrollment = enrollmentRepository.findById(
                completion.getEnrollment().getEnrollmentId()
        ).orElseThrow(() ->
                new EntityNotFoundException("Matrícula não encontrada para conclusão.")
        );

        if (enrollment.getCompletion() != null) {
            throw new IllegalStateException("Esta matrícula já possui uma conclusão registrada.");
        }

        if (completion.getCompletionDate() == null) {
            completion.setCompletionDate(LocalDate.now());
        }

        validateResult(completion.getResult());

        completion.setResult(completion.getResult().toUpperCase());

        completion.setEnrollment(enrollment);
        enrollment.setCompletion(completion);

        return completionRepository.save(completion);
    }

    public Completion updateCompletion(String id, Completion updatedCompletion) {
        Completion existing = completionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conclusão não encontrada."));

        if (updatedCompletion.getCompletionDate() != null) {
            existing.setCompletionDate(updatedCompletion.getCompletionDate());
        }

        if (updatedCompletion.getResult() != null && !updatedCompletion.getResult().isBlank()) {
            validateResult(updatedCompletion.getResult());
            existing.setResult(updatedCompletion.getResult().toUpperCase());
        }

        return completionRepository.save(existing);
    }

    public void deleteCompletion(String id) {
        Completion completion = completionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conclusão não encontrada."));

        Enrollment enrollment = completion.getEnrollment();
        if (enrollment != null) {
            enrollment.setCompletion(null);
        }

        completionRepository.delete(completion);
    }

    private void validateResult(String result) {
        if (result == null || result.isBlank()) {
            throw new IllegalArgumentException("O resultado da conclusão é obrigatório.");
        }

        if (!VALID_RESULTS.contains(result.toUpperCase())) {
            throw new IllegalArgumentException("Resultado inválido. Use: COMPLETO, INCOMPLETO, APROVADO ou REPROVADO.");
        }
    }
}