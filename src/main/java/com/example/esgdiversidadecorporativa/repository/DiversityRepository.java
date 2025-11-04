package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.DiversityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiversityRepository extends JpaRepository<DiversityRepository, Long> {
}
