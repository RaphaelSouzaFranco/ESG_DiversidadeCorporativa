package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Diversity;
import com.example.esgdiversidadecorporativa.repository.DiversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiversityService {

    private final DiversityRepository diversityRepository;

    @Autowired
    public DiversityService(DiversityRepository diversityRepository) {
        this.diversityRepository = diversityRepository;
    }

    public List<Diversity> findAll() {
        return diversityRepository.findAll();
    }

    public Optional<Diversity> findById(Long id) {
        return diversityRepository.findById(id);
    }

    public Diversity save(Diversity diversity) {
        return diversityRepository.save(diversity);
    }

    public void deleteById(Long id) {
        diversityRepository.deleteById(id);
    }
}
