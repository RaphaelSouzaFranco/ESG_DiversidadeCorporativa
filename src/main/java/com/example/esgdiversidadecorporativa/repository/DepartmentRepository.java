package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // Verifica se já existe um departamento com o nome informado
    boolean existsByName(String name);
}
