package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.model.Email;
import com.example.esgdiversidadecorporativa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public List<Email> getAllEmails() {
        return emailService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        return emailService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Email createEmail(@RequestBody Email email) {
        return emailService.save(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody Email updatedEmail) {
        return emailService.findById(id)
                .map(existing -> {
                    updatedEmail.setId(id); // garanta que o ID permaneça o mesmo
                    return ResponseEntity.ok(emailService.save(updatedEmail));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
