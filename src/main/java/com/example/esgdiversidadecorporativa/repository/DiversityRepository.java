package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Diversity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiversityRepository extends JpaRepository<DiversityRepository, Long> {
}
