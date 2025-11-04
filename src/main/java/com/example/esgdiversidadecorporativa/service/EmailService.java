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

    public List<Email> findAll() {
        return emailRepository.findAll();
    }

    public Optional<Email> findById(Long id) {
        return emailRepository.findById(id);
    }

    public Email save(Email email) {
        return emailRepository.save(email);
    }

    public void deleteById(Long id) {
        emailRepository.deleteById(id);
    }
}
