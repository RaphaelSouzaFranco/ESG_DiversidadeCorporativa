package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.entity.Diversity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DiversityRepository extends JpaRepository<Diversity, String> {

    // Busca relatório de diversidade pelo departamento
    Optional<Diversity> findByDepartment(Department department);
}
