package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.dto.DiversityDto;
import com.example.esgdiversidadecorporativa.model.Department;
import com.example.esgdiversidadecorporativa.model.Diversity;
import com.example.esgdiversidadecorporativa.repository.DiversityRepository;
import com.example.esgdiversidadecorporativa.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiversityService {

    private final DiversityRepository diversityRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DiversityService(DiversityRepository diversityRepository, DepartmentRepository departmentRepository) {
        this.diversityRepository = diversityRepository;
        this.departmentRepository = departmentRepository;
    }

    // Converter Entity -> DTO
    private DiversityDto toDTO(Diversity diversity) {
        DiversityDto dto = new DiversityDto();
        dto.setReportId(diversity.getReportId());
        dto.setDepartmentId(diversity.getDepartment().getDepartmentId());
        dto.setTotalEmployees(diversity.getTotalEmployees());
        dto.setMaleCount(diversity.getMaleCount());
        dto.setFemaleCount(diversity.getFemaleCount());
        dto.setOtherCount(diversity.getOtherCount());
        dto.setPercentageMale(diversity.getPercentageMale());
        dto.setPercentageFemale(diversity.getPercentageFemale());
        dto.setPercentageOther(diversity.getPercentageOther());
        return dto;
    }

    // Converter DTO -> Entity
    private Diversity fromDTO(DiversityDto dto) {
        Optional<Department> deptOpt = departmentRepository.findById(dto.getDepartmentId());
        if (deptOpt.isEmpty()) {
            throw new RuntimeException("Departamento não encontrado");
        }

        Diversity diversity = new Diversity();
        diversity.setReportId(dto.getReportId());
        diversity.setDepartment(deptOpt.get());
        diversity.setTotalEmployees(dto.getTotalEmployees());
        diversity.setMaleCount(dto.getMaleCount());
        diversity.setFemaleCount(dto.getFemaleCount());
        diversity.setOtherCount(dto.getOtherCount());
        diversity.calculatePercentages();
        return diversity;
    }

    // Operações CRUD usando DTOs
    public List<DiversityDto> findAll() {
        return diversityRepository.findAll()
                .stream()
                .filter(Diversity::hasGoodDiversity)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DiversityDto> findById(String id) {
        return diversityRepository.findById(id)
                .map(this::toDTO);
    }

    public DiversityDto save(DiversityDto dto) {
        Diversity saved = diversityRepository.save(fromDTO(dto));
        return toDTO(saved);
    }

    public void deleteById(String id) {
        diversityRepository.deleteById(id);
    }
}
