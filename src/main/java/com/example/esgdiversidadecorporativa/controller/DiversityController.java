package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.model.Diversity;
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
    public List<Diversity> getAll() {
        return diversityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diversity> getById(@PathVariable Long id) {
        return diversityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Diversity create(@RequestBody Diversity diversity) {
        return diversityService.save(diversity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diversity> update(@PathVariable Long id, @RequestBody Diversity updated) {
        return diversityService.findById(id)
                .map(existing -> {
                    updated.setReportId(id);
                    return ResponseEntity.ok(diversityService.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diversityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
