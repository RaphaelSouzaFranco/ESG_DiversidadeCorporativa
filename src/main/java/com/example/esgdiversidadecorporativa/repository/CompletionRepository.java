package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Completion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends JpaRepository<CompletionRepository, Long> {
}
