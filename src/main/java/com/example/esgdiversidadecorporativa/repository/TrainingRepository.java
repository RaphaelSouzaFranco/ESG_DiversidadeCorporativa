package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, String> {
    boolean existsByTitle(String title);
}

