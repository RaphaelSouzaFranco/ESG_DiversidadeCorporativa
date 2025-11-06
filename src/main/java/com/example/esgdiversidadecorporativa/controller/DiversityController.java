package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.dto.DiversityDto;
import com.example.esgdiversidadecorporativa.service.DiversityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diversities")
public class DiversityController {

    private final DiversityService diversityService;

    @Autowired
    public DiversityController(DiversityService diversityService) {
        this.diversityService = diversityService;
    }

    @GetMapping
    public List<DiversityDto> getAll() {
        return diversityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiversityDto> getById(@PathVariable String id) {
        return diversityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DiversityDto> create(@RequestBody DiversityDto dto) {
        DiversityDto saved = diversityService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiversityDto> update(@PathVariable String id, @RequestBody DiversityDto dto) {
        return diversityService.findById(id)
                .map(existing -> {
                    dto.setReportId(id); // garante que o ID não será alterado
                    DiversityDto updated = diversityService.save(dto);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        diversityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
