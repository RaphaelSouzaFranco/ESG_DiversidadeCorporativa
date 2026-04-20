package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.DepartmentDto;
import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId("DEPT-1");
        department.setName("Finance");

        departmentDto = new DepartmentDto("DEPT-1", "Finance");
    }

    @Test
    void testCreateDepartmentSuccess() {
        when(departmentRepository.existsByName("Finance")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto result = departmentService.createDepartment(departmentDto);
        assertNotNull(result);
        assertEquals("DEPT-1", result.getDepartmentId());
    }

    @Test
    void testCreateDepartmentAlreadyExists() {
        when(departmentRepository.existsByName("Finance")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> departmentService.createDepartment(departmentDto));
    }

    @Test
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        List<DepartmentDto> results = departmentService.getAllDepartments();
        assertEquals(1, results.size());
    }

    @Test
    void testGetDepartmentById() {
        when(departmentRepository.findById("DEPT-1")).thenReturn(Optional.of(department));
        Optional<DepartmentDto> result = departmentService.getDepartmentById("DEPT-1");
        assertTrue(result.isPresent());
    }

    @Test
    void testUpdateDepartmentSuccess() {
        when(departmentRepository.findById("DEPT-1")).thenReturn(Optional.of(department));
        when(departmentRepository.existsByName("HR")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto updatedDto = new DepartmentDto("DEPT-1", "HR");
        DepartmentDto result = departmentService.updateDepartment("DEPT-1", updatedDto);

        assertEquals("HR", result.getName());
    }

    @Test
    void testDeleteDepartmentSuccess() {
        when(departmentRepository.findById("DEPT-1")).thenReturn(Optional.of(department));
        assertDoesNotThrow(() -> departmentService.deleteDepartment("DEPT-1"));
        verify(departmentRepository, times(1)).delete(department);
    }
}
