package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Enrollment;
import com.example.esgdiversidadecorporativa.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    // Injeção de dependência via construtor
    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    // Buscar todos os registros
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    // Buscar por ID
    public Optional<Enrollment> findById(String id) {
        return enrollmentRepository.findById(id);
    }

    // Salvar novo ou atualizar existente
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // Deletar por ID
    public void deleteById(String id) {
        enrollmentRepository.deleteById(id);
    }
}
