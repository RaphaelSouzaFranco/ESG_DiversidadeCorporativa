package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.model.Training;
import com.example.esgdiversidadecorporativa.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public List<Training> getAll() {
        return trainingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> getById(@PathVariable Long id) {
        return trainingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Training create(@RequestBody Training training) {
        return trainingService.save(training);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> update(@PathVariable Long id, @RequestBody Training updated) {
        return trainingService.findById(id)
                .map(existing -> {
                    updated.setTrainingId(id);
                    return ResponseEntity.ok(trainingService.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trainingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
