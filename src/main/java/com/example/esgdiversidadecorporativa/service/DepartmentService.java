package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.DepartmentDto;
import com.example.esgdiversidadecorporativa.model.Department;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // ---------------------------
    // Criar novo departamento
    // ---------------------------
    public DepartmentDto createDepartment(DepartmentDto dto) {
        if (departmentRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Já existe um departamento com esse nome.");
        }

        Department department = new Department();
        department.setDepartmentId(dto.getDepartmentId() != null ? dto.getDepartmentId() : UUID.randomUUID().toString());
        department.setName(dto.getName());

        departmentRepository.save(department);

        dto.setDepartmentId(department.getDepartmentId());
        return dto;
    }

    // ---------------------------
    // Listar todos os departamentos
    // ---------------------------
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(d -> new DepartmentDto(d.getDepartmentId(), d.getName()))
                .collect(Collectors.toList());
    }

    // ---------------------------
    // Buscar por ID
    // ---------------------------
    public Optional<DepartmentDto> getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .map(d -> new DepartmentDto(d.getDepartmentId(), d.getName()));
    }

    // ---------------------------
    // Atualizar departamento
    // ---------------------------
    public DepartmentDto updateDepartment(String id, DepartmentDto dto) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado."));

        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(existing.getName())) {
            if (departmentRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Já existe um departamento com esse nome.");
            }
            existing.setName(dto.getName());
        }

        departmentRepository.save(existing);

        return new DepartmentDto(existing.getDepartmentId(), existing.getName());
    }

    // ---------------------------
    // Deletar departamento
    // ---------------------------
    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado."));
        departmentRepository.delete(department);
    }
}
