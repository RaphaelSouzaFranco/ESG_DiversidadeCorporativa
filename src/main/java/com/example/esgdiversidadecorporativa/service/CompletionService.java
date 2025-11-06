package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Completion;
import com.example.esgdiversidadecorporativa.model.Enrollment;
import com.example.esgdiversidadecorporativa.repository.CompletionRepository;
import com.example.esgdiversidadecorporativa.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompletionService {

    private final CompletionRepository completionRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public CompletionService(CompletionRepository completionRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.completionRepository = completionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // 🔍 Buscar todas as conclusões
    public List<Completion> findAll() {
        return completionRepository.findAll();
    }

    // 🔍 Buscar por ID
    public Optional<Completion> findById(String id) {
        return completionRepository.findById(id);
    }

    // 💾 Criar uma nova conclusão
    public Completion createCompletion(Completion completion) {

        if (completion.getEnrollment() == null || completion.getEnrollment().getEnrollmentId() == null) {
            throw new IllegalArgumentException("Uma conclusão precisa estar associada a uma matrícula válida.");
        }

        Enrollment enrollment = enrollmentRepository.findById(
                completion.getEnrollment().getEnrollmentId().toString()
        ).orElseThrow(() ->
                new EntityNotFoundException("Matrícula não encontrada para conclusão.")
        );


        if (enrollment.getCompletion() != null) {
            throw new IllegalStateException("Esta matrícula já possui uma conclusão registrada.");
        }


        if (completion.getCompletionDate() == null) {
            completion.setCompletionDate(LocalDate.now());
        }

        // 🧠 Regra 4: resultado deve ser válido
        if (completion.getResult() == null || completion.getResult().isBlank()) {
            throw new IllegalArgumentException("O resultado da conclusão é obrigatório (ex: Aprovado, Reprovado).");
        }

        // associa bidirecionalmente
        completion.setEnrollment(enrollment);
        enrollment.setCompletion(completion);

        return completionRepository.save(completion);
    }

    // ✏️ Atualizar conclusão existente
    public Completion updateCompletion(String id, Completion updatedCompletion) {
        Completion existing = completionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conclusão não encontrada."));

        // só atualiza campos relevantes
        existing.setCompletionDate(updatedCompletion.getCompletionDate() != null
                ? updatedCompletion.getCompletionDate()
                : existing.getCompletionDate());

        existing.setResult(updatedCompletion.getResult() != null
                ? updatedCompletion.getResult()
                : existing.getResult());

        return completionRepository.save(existing);
    }

    // ❌ Deletar conclusão
    public void deleteCompletion(String id) {
        Completion completion = completionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conclusão não encontrada."));
        completionRepository.delete(completion);
    }
}
