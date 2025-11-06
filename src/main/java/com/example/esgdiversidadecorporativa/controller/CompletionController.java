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

    // 🔹 GET - Listar todas as conclusões
    @GetMapping
    public ResponseEntity<List<Completion>> getAllCompletions() {
        List<Completion> completions = completionService.findAll();
        return ResponseEntity.ok(completions);
    }

    // 🔹 GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Completion> getCompletionById(@PathVariable String id) {
        return completionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 POST - Criar nova conclusão
    @PostMapping
    public ResponseEntity<?> createCompletion(@RequestBody Completion completion) {
        try {
            Completion saved = completionService.createCompletion(completion);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar conclusão: " + e.getMessage());
        }
    }

    // 🔹 PUT - Atualizar conclusão existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompletion(@PathVariable String id, @RequestBody Completion updated) {
        try {
            Completion saved = completionService.updateCompletion(id, updated);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar: " + e.getMessage());
        }
    }

    // 🔹 DELETE - Deletar conclusão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompletion(@PathVariable String id) {
        completionService.deleteCompletion(id);
        return ResponseEntity.noContent().build();
    }
}
