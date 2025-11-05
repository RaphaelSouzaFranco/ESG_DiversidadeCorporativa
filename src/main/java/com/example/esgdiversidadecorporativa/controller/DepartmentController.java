package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.dto.DepartmentDto;
import com.example.esgdiversidadecorporativa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // ---------------------------
    // Listar todos os departamentos
    // ---------------------------
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // ---------------------------
    // Buscar por ID
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable String id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------
    // Criar novo departamento
    // ---------------------------
    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto dto) {
        DepartmentDto created = departmentService.createDepartment(dto);
        return ResponseEntity.ok(created);
    }

    // ---------------------------
    // Atualizar departamento
    // ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable String id, @RequestBody DepartmentDto dto) {
        try {
            DepartmentDto updated = departmentService.updateDepartment(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------------------
    // Deletar departamento
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
