package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Training;
import com.example.esgdiversidadecorporativa.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;

    // Injeção de dependência via construtor
    @Autowired
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    // Buscar todos os registros
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    // Buscar por ID
    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    // Salvar novo ou atualizar existente
    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    // Deletar por ID
    public void deleteById(Long id) {
        trainingRepository.deleteById(id);
    }
}
