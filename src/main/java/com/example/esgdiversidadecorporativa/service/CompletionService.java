package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Completion;
import com.example.esgdiversidadecorporativa.repository.CompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompletionService {

    private final CompletionRepository completionRepository;

    // Injeção de dependência via construtor
    @Autowired
    public CompletionService(CompletionRepository completionRepository) {
        this.completionRepository = completionRepository;
    }

    // Buscar todos os registros
    public List<Completion> findAll() {
        return completionRepository.findAll();
    }

    // Buscar por ID
    public Optional<Completion> findById(Long id) {
        return completionRepository.findById(id);
    }

    // Salvar novo ou atualizar existente
    public Completion save(Completion completion) {
        return completionRepository.save(completion);
    }

    // Deletar por ID
    public void deleteById(Long id) {
        completionRepository.deleteById(id);
    }
}
