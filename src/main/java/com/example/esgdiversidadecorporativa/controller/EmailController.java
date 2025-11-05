package com.example.esgdiversidadecorporativa.controller;

import com.example.esgdiversidadecorporativa.dto.EmailDto;
import com.example.esgdiversidadecorporativa.model.Email;
import com.example.esgdiversidadecorporativa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    public ResponseEntity<Email> getEmailById(@PathVariable String id) {
        return emailService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Email> createEmail(@Valid @RequestBody EmailDto dto) {
        Email email = new Email();
        email.setRecipient(dto.getRecipient());
        email.setSubject(dto.getSubject());
        email.setBody(dto.getBody());
        email.setStatus("PENDING");
        return ResponseEntity.ok(emailService.save(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable String id, @Valid @RequestBody EmailDto dto) {
        return emailService.findById(id)
                .map(existing -> {
                    if (existing.isSent()) {
                        return ResponseEntity.badRequest().body(existing);
                    }
                    existing.setRecipient(dto.getRecipient());
                    existing.setSubject(dto.getSubject());
                    existing.setBody(dto.getBody());
                    return ResponseEntity.ok(emailService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable String id) {
        emailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
