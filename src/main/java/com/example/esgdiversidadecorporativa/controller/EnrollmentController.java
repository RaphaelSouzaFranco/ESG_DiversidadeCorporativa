package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.model.Enrollment;
import com.example.esgdiversidadecorporativa.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAll() {
        return enrollmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getById(@PathVariable String id) {
        return enrollmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Enrollment create(@Valid @RequestBody Enrollment enrollment) {
        return enrollmentService.save(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> update(@PathVariable String id, @Valid @RequestBody Enrollment updated) {
        return enrollmentService.findById(id)
                .map(existing -> {
                    updated.setEnrollmentId(id);
                    return ResponseEntity.ok(enrollmentService.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
