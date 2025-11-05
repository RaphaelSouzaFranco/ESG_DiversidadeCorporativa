package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.model.Email;
import com.example.esgdiversidadecorporativa.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    // Buscar todos os emails
    public List<Email> findAll() {
        return emailRepository.findAll();
    }

    // Buscar por ID
    public Optional<Email> findById(String id) {
        return emailRepository.findById(id);
    }

    // Criar ou atualizar email
    public Email save(Email email) {

        // Valida status permitido
        if (email.getStatus() != null) {
            String status = email.getStatus().toUpperCase();
            if (!List.of("PENDING", "SENT", "FAILED").contains(status)) {
                throw new IllegalArgumentException("Status inválido: " + email.getStatus());
            }
        }

        // Bloqueia alteração de email já enviado
        if (email.getId() != null) {
            Optional<Email> existing = emailRepository.findById(email.getId());
            if (existing.isPresent() && existing.get().isSent()) {
                throw new IllegalStateException("Não é possível alterar um e-mail já enviado.");
            }
        }

        return emailRepository.save(email);
    }

    // Excluir email
    public void deleteById(String id) {
        Optional<Email> existing = emailRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("E-mail não encontrado: " + id);
        }

        if (existing.get().isSent()) {
            throw new IllegalStateException("Não é permitido excluir um e-mail já enviado.");
        }

        emailRepository.deleteById(id);
    }

    // Buscar emails pendentes
    public List<Email> findPending() {
        return emailRepository.findByStatus("PENDING");
    }

    // Simular envio e marcar como enviado
    public Email markAsSent(String id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("E-mail não encontrado: " + id));

        email.markAsSent();
        return emailRepository.save(email);
    }
}
