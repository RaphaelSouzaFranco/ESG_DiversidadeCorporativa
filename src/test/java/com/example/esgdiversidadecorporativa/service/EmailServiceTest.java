package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.entity.Email;
import com.example.esgdiversidadecorporativa.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    private Email email;

    @BeforeEach
    void setUp() {
        email = new Email();
        email.setId("EMAIL-1");
        email.setStatus("PENDING");
    }

    @Test
    void testFindAll() {
        when(emailRepository.findAll()).thenReturn(List.of(email));
        List<Email> results = emailService.findAll();
        assertEquals(1, results.size());
    }

    @Test
    void testFindById() {
        when(emailRepository.findById("EMAIL-1")).thenReturn(Optional.of(email));
        Optional<Email> result = emailService.findById("EMAIL-1");
        assertTrue(result.isPresent());
    }

    @Test
    void testSaveSuccess() {
        when(emailRepository.save(any(Email.class))).thenReturn(email);
        Email result = emailService.save(email);
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testSaveInvalidStatus() {
        email.setStatus("INVALID");
        assertThrows(IllegalArgumentException.class, () -> emailService.save(email));
    }

    @Test
    void testSaveAlreadySent() {
        email.setStatus("SENT");
        email.markAsSent();
        when(emailRepository.findById("EMAIL-1")).thenReturn(Optional.of(email));
        assertThrows(IllegalStateException.class, () -> emailService.save(email));
    }

    @Test
    void testDeleteByIdSuccess() {
        when(emailRepository.findById("EMAIL-1")).thenReturn(Optional.of(email));
        assertDoesNotThrow(() -> emailService.deleteById("EMAIL-1"));
        verify(emailRepository, times(1)).deleteById("EMAIL-1");
    }

    @Test
    void testDeleteByIdAlreadySent() {
        email.setStatus("SENT");
        email.markAsSent();
        when(emailRepository.findById("EMAIL-1")).thenReturn(Optional.of(email));
        assertThrows(IllegalStateException.class, () -> emailService.deleteById("EMAIL-1"));
    }

    @Test
    void testFindPending() {
        when(emailRepository.findByStatus("PENDING")).thenReturn(List.of(email));
        List<Email> results = emailService.findPending();
        assertEquals(1, results.size());
    }

    @Test
    void testMarkAsSentSuccess() {
        when(emailRepository.findById("EMAIL-1")).thenReturn(Optional.of(email));
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email result = emailService.markAsSent("EMAIL-1");
        assertTrue(result.isSent());
    }
}
