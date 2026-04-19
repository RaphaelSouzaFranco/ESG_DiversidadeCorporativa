package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.EmployeeDto;
import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.entity.Employee;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import com.example.esgdiversidadecorporativa.repository.DiversityRepository;
import com.example.esgdiversidadecorporativa.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DiversityRepository diversityRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Department department;
    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId("DEPT-001");
        department.setName("TI");

        employee = new Employee();
        employee.setEmployeeId("EMP-001");
        employee.setName("Nicolly");
        employee.setEmail("nicolly@empresa.com");
        employee.setGender("F");
        employee.setDepartment(department);
        employee.setEnrollments(new ArrayList<>());

        employeeDto = new EmployeeDto();
        employeeDto.setName("Nicolly");
        employeeDto.setEmail("nicolly@empresa.com");
        employeeDto.setGender("F");
        employeeDto.setDepartmentId("DEPT-001");
    }

    // -------------------------------------------------------
    // createEmployee
    // -------------------------------------------------------

    @Test
    void deveCriarFuncionarioComSucesso() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById("DEPT-001")).thenReturn(Optional.of(department));
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeRepository.findByDepartment(any())).thenReturn(List.of(employee));
        when(diversityRepository.findByDepartment(any())).thenReturn(Optional.empty());

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        assertNotNull(result);
        assertEquals("nicolly@empresa.com", result.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        when(employeeRepository.existsByEmail("nicolly@empresa.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.createEmployee(employeeDto));

        assertEquals("Já existe um funcionario com este e-mail.", ex.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoGeneroInvalido() {
        employeeDto.setGender("X");
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.createEmployee(employeeDto));

        assertEquals("Gênero inválido. Use apenas 'M', 'F' ou 'O'.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDepartamentoNaoEncontrado() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById("DEPT-001")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.createEmployee(employeeDto));

        assertEquals("Departamento não encontrado.", ex.getMessage());
    }

    @Test
    void deveAceitarGenerosValidos() {
        for (String gender : List.of("M", "F", "O", "m", "f", "o")) {
            employeeDto.setGender(gender);
            when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
            when(departmentRepository.findById(anyString())).thenReturn(Optional.of(department));
            when(employeeRepository.save(any())).thenReturn(employee);
            when(employeeRepository.findByDepartment(any())).thenReturn(List.of());
            when(diversityRepository.findByDepartment(any())).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> employeeService.createEmployee(employeeDto));
        }
    }

    // -------------------------------------------------------
    // getAllEmployees
    // -------------------------------------------------------

    @Test
    void deveRetornarListaDeFuncionarios() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = employeeService.getAllEmployees();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Nicolly", result.get(0).getName());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaFuncionarios() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<Employee> result = employeeService.getAllEmployees();

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------
    // getEmployeeById
    // -------------------------------------------------------

    @Test
    void deveRetornarFuncionarioPorId() {
        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById("EMP-001");

        assertTrue(result.isPresent());
        assertEquals("Nicolly", result.get().getName());
    }

    @Test
    void deveRetornarVazioQuandoFuncionarioNaoEncontrado() {
        when(employeeRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById("NAO-EXISTE");

        assertFalse(result.isPresent());
    }

    // -------------------------------------------------------
    // updateEmployee
    // -------------------------------------------------------

    @Test
    void deveAtualizarFuncionarioComSucesso() {
        EmployeeDto update = new EmployeeDto();
        update.setName("Nicolly Atualizada");
        update.setEmail("novo@empresa.com");
        update.setGender("F");

        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("novo@empresa.com")).thenReturn(false);
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeRepository.findByDepartment(any())).thenReturn(List.of(employee));
        when(diversityRepository.findByDepartment(any())).thenReturn(Optional.empty());

        Employee result = employeeService.updateEmployee("EMP-001", update);

        assertNotNull(result);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarFuncionarioInexistente() {
        when(employeeRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.updateEmployee("NAO-EXISTE", employeeDto));
    }

    @Test
    void deveLancarExcecaoSeEmailJaEmUsoNaAtualizacao() {
        EmployeeDto update = new EmployeeDto();
        update.setEmail("outro@empresa.com");

        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("outro@empresa.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.updateEmployee("EMP-001", update));
    }

    // -------------------------------------------------------
    // deleteEmployee
    // -------------------------------------------------------

    @Test
    void deveDeletarFuncionarioComSucesso() {
        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));
        when(employeeRepository.findByDepartment(any())).thenReturn(List.of());
        when(diversityRepository.findByDepartment(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> employeeService.deleteEmployee("EMP-001"));

        verify(employeeRepository).delete(employee);
    }

    @Test
    void deveLancarExcecaoAoDeletarFuncionarioInexistente() {
        when(employeeRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.deleteEmployee("NAO-EXISTE"));

        verify(employeeRepository, never()).delete(any());
    }
}