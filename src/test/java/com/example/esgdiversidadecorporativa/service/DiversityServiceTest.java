package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.DiversityDto;
import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.entity.Diversity;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import com.example.esgdiversidadecorporativa.repository.DiversityRepository;
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
class DiversityServiceTest {

    @Mock
    private DiversityRepository diversityRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DiversityService diversityService;

    private Diversity diversity;
    private DiversityDto diversityDto;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId("DEPT-1");

        diversity = new Diversity();
        diversity.setReportId("DIV-1");
        diversity.setDepartment(department);
        diversity.setTotalEmployees(100L);
        diversity.setMaleCount(40L);
        diversity.setFemaleCount(40L);
        diversity.setOtherCount(20L);
        diversity.calculatePercentages();

        diversityDto = new DiversityDto();
        diversityDto.setReportId("DIV-1");
        diversityDto.setDepartmentId("DEPT-1");
        diversityDto.setTotalEmployees(100L);
        diversityDto.setMaleCount(40L);
        diversityDto.setFemaleCount(40L);
        diversityDto.setOtherCount(20L);
    }

    @Test
    void testFindAll() {
        when(diversityRepository.findAll()).thenReturn(List.of(diversity));
        List<DiversityDto> results = diversityService.findAll();
        assertEquals(1, results.size());
    }

    @Test
    void testFindById() {
        when(diversityRepository.findById("DIV-1")).thenReturn(Optional.of(diversity));
        Optional<DiversityDto> result = diversityService.findById("DIV-1");
        assertTrue(result.isPresent());
    }

    @Test
    void testSaveSuccess() {
        when(departmentRepository.findById("DEPT-1")).thenReturn(Optional.of(department));
        when(diversityRepository.save(any(Diversity.class))).thenReturn(diversity);

        DiversityDto result = diversityService.save(diversityDto);
        assertNotNull(result);
        assertEquals("DIV-1", result.getReportId());
    }

    @Test
    void testSaveDepartmentNotFound() {
        when(departmentRepository.findById("DEPT-1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> diversityService.save(diversityDto));
    }

    @Test
    void testDeleteById() {
        assertDoesNotThrow(() -> diversityService.deleteById("DIV-1"));
        verify(diversityRepository, times(1)).deleteById("DIV-1");
    }
}
