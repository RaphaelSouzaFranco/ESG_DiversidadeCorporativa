package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Training;
import com.example.esgdiversidadecorporativa.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Optional<Training> findById(String id) {
        return trainingRepository.findById(id);
    }

    @Transactional
    public Training save(Training training) {

        // Regra 1: gerar ID automático se não for informado
        if (training.getTrainingId() == null || training.getTrainingId().isBlank()) {
            training.setTrainingId("TRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        // Regra extra: não permitir editar treinamento vencido
        if (training.getTrainingId() != null) {
            Optional<Training> existing = trainingRepository.findById(training.getTrainingId());
            if (existing.isPresent() && existing.get().isOverdue()) {
                throw new IllegalStateException("Não é possível alterar um treinamento cuja data já expirou.");
            }
        }
        // Regra 2: validar unicidade do título
        if (trainingRepository.existsByTitle(training.getTitle())) {
            throw new IllegalArgumentException("Já existe um treinamento com este título: " + training.getTitle());
        }

        // Regra 3: dueDate deve ser uma data futura
        if (training.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de conclusão (dueDate) não pode estar no passado.");
        }

        // Regra 4: salvar
        return trainingRepository.save(training);
    }

    public void deleteById(String id) {
        Optional<Training> training = trainingRepository.findById(id);

        if (training.isEmpty()) {
            throw new IllegalArgumentException("Treinamento não encontrado: " + id);
        }

        // Regra 5: impedir exclusão se tiver matrículas associadas
        if (!training.get().getEnrollments().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um treinamento com matrículas associadas.");
        }

        trainingRepository.deleteById(id);
    }
}
