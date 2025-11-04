package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.model.Completion;
import com.example.esgdiversidadecorporativa.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/completions")
public class CompletionController {

    private final CompletionService completionService;

    @Autowired
    public CompletionController(CompletionService completionService) {
        this.completionService = completionService;
    }

    @GetMapping
    public List<Completion> getAll() {
        return completionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Completion> getById(@PathVariable Long id) {
        return completionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Completion create(@RequestBody Completion completion) {
        return completionService.save(completion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Completion> update(@PathVariable Long id, @RequestBody Completion updated) {
        return completionService.findById(id)
                .map(existing -> {
                    updated.setCompletionId(id);
                    return ResponseEntity.ok(completionService.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        completionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
