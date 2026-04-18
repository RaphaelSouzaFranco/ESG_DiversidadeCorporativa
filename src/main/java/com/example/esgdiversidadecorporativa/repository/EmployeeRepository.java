package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // Verifica se já existe um funcionário com o e-mail informado
    boolean existsByEmail(String email);

    // Retorna todos os funcionários de um determinado departamento
    List<Employee> findByDepartment(Department department);
}
