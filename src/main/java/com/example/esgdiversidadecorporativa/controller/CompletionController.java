package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.entity.Completion;
import com.example.esgdiversidadecorporativa.service.CompletionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Completion>> getAllCompletions() {
        List<Completion> completions = completionService.findAll();
        return ResponseEntity.ok(completions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Completion> getCompletionById(@PathVariable String id) {
        return completionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCompletion(@RequestBody Completion completion) {
        try {
            Completion saved = completionService.createCompletion(completion);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar conclusão: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompletion(@PathVariable String id, @RequestBody Completion updated) {
        try {
            Completion saved = completionService.updateCompletion(id, updated);
            return ResponseEntity.ok(saved);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompletion(@PathVariable String id) {
        try {
            completionService.deleteCompletion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar: " + e.getMessage());
        }
    }
}