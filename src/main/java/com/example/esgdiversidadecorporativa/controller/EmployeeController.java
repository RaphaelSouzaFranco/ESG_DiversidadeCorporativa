package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.dto.EmployeeDto;
import com.example.esgdiversidadecorporativa.model.Employee;
import com.example.esgdiversidadecorporativa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ---------------------------
    // Listar todos
    // ---------------------------
    @GetMapping
    public List<Employee> getAll() {
        return employeeService.getAllEmployees();
    }

    // ---------------------------
    // Buscar por ID
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------
    // Criar funcionário
    // ---------------------------
    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    // ---------------------------
    // Atualizar funcionário
    // ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable String id, @RequestBody EmployeeDto dto) {
        try {
            return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------------------
    // Deletar funcionário
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
