package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Employee;
import com.example.esgdiversidadecorporativa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Injeção de dependência via construtor
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Buscar todos os registros
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // Buscar por ID
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    // Salvar novo ou atualizar existente
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Deletar por ID
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
