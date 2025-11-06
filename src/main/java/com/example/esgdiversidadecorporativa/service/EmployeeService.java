package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.EmployeeDto;
import com.example.esgdiversidadecorporativa.model.Department;
import com.example.esgdiversidadecorporativa.model.Diversity;
import com.example.esgdiversidadecorporativa.model.Employee;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import com.example.esgdiversidadecorporativa.repository.DiversityRepository;
import com.example.esgdiversidadecorporativa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DiversityRepository diversityReportRepository;

    // ---------------------------
    // Cadastrar novo funcionário
    // ---------------------------
    public EmployeeDto createEmployee(EmployeeDto dto) {

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um funcionário com este e-mail.");
        }

        if (!isValidGender(dto.getGender())) {
            throw new IllegalArgumentException("Gênero inválido. Use apenas 'M', 'F' ou 'O'.");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado."));

        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setGender(dto.getGender());
        employee.setDepartment(department);

        employeeRepository.save(employee);

        updateDiversity(department);

        dto.setEmployeeId(employee.getEmployeeId());
        return dto;
    }

    // ---------------------------
    // Listar todos os funcionários
    // ---------------------------
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // ---------------------------
    // Buscar por ID
    // ---------------------------
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    // ---------------------------
    // Atualizar funcionário
    // ---------------------------
    public Employee updateEmployee(String id, EmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            if (employeeRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("E-mail já em uso por outro funcionário.");
            }
            existing.setEmail(dto.getEmail());
        }

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getGender() != null && isValidGender(dto.getGender())) existing.setGender(dto.getGender());

        if (dto.getDepartmentId() != null) {
            Department newDept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado."));
            existing.setDepartment(newDept);
        }

        employeeRepository.save(existing);
        updateDiversity(existing.getDepartment());

        return existing;
    }

    // ---------------------------
    // Deletar funcionário
    // ---------------------------
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

        Department dept = employee.getDepartment();
        employeeRepository.delete(employee);
        updateDiversity(dept);
    }

    // ---------------------------
    // Função auxiliar: validar gênero
    // ---------------------------
    private boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("M")
                || gender.equalsIgnoreCase("F")
                || gender.equalsIgnoreCase("O"));
    }

    // ---------------------------
    // Função auxiliar: atualizar relatório de diversidade
    // ---------------------------
    private void updateDiversity(Department department) {
        List<Employee> employees = employeeRepository.findByDepartment(department);

        long total = employees.size();
        long maleCount = employees.stream().filter(e -> "M".equalsIgnoreCase(e.getGender())).count();
        long femaleCount = employees.stream().filter(e -> "F".equalsIgnoreCase(e.getGender())).count();
        long otherCount = employees.stream().filter(e -> "O".equalsIgnoreCase(e.getGender())).count();

        Diversity report = diversityReportRepository.findByDepartment(department)
                .orElseGet(() -> {
                    Diversity newReport = new Diversity();
                    newReport.setReportId(UUID.randomUUID().toString());
                    newReport.setDepartment(department);
                    newReport.setCreatedAt(LocalDateTime.now());
                    return newReport;
                });

        report.setTotalEmployees(total);
        report.setMaleCount(maleCount);
        report.setFemaleCount(femaleCount);
        report.setOtherCount(otherCount);

        diversityReportRepository.save(report);
    }
}
