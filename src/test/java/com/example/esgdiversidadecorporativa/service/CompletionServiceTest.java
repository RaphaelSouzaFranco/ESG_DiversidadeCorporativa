package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.entity.Completion;
import com.example.esgdiversidadecorporativa.entity.Enrollment;
import com.example.esgdiversidadecorporativa.repository.CompletionRepository;
import com.example.esgdiversidadecorporativa.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class CompletionServiceTest {

    @Mock
    private CompletionRepository completionRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CompletionService completionService;

    private Completion completion;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR-1");
        
        completion = new Completion();
        completion.setCompletionId("CMP-1");
        completion.setResult("COMPLETO");
        completion.setEnrollment(enrollment);
    }

    @Test
    void testFindAll() {
        when(completionRepository.findAll()).thenReturn(List.of(completion));
        List<Completion> results = completionService.findAll();
        assertEquals(1, results.size());
    }

    @Test
    void testFindById() {
        when(completionRepository.findById("CMP-1")).thenReturn(Optional.of(completion));
        Optional<Completion> result = completionService.findById("CMP-1");
        assertTrue(result.isPresent());
    }

    @Test
    void testCreateCompletionSuccess() {
        when(enrollmentRepository.findById("ENR-1")).thenReturn(Optional.of(enrollment));
        when(completionRepository.save(any(Completion.class))).thenReturn(completion);

        Completion result = completionService.createCompletion(completion);
        
        assertNotNull(result);
        assertEquals("COMPLETO", result.getResult());
        verify(completionRepository, times(1)).save(completion);
    }

    @Test
    void testCreateCompletionWithoutEnrollment() {
        completion.setEnrollment(null);
        assertThrows(IllegalArgumentException.class, () -> completionService.createCompletion(completion));
    }
    
    @Test
    void testCreateCompletionEnrollmentNotFound() {
        when(enrollmentRepository.findById("ENR-1")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> completionService.createCompletion(completion));
    }
    
    @Test
    void testCreateCompletionAlreadyExists() {
        enrollment.setCompletion(new Completion());
        when(enrollmentRepository.findById("ENR-1")).thenReturn(Optional.of(enrollment));
        assertThrows(IllegalStateException.class, () -> completionService.createCompletion(completion));
    }
    
    @Test
    void testUpdateCompletionSuccess() {
        when(completionRepository.findById("CMP-1")).thenReturn(Optional.of(completion));
        when(completionRepository.save(any(Completion.class))).thenReturn(completion);
        
        Completion updated = new Completion();
        updated.setResult("APROVADO");
        
        Completion result = completionService.updateCompletion("CMP-1", updated);
        assertEquals("APROVADO", result.getResult());
    }
    
    @Test
    void testDeleteCompletionSuccess() {
        when(completionRepository.findById("CMP-1")).thenReturn(Optional.of(completion));
        
        assertDoesNotThrow(() -> completionService.deleteCompletion("CMP-1"));
        verify(completionRepository, times(1)).delete(completion);
        assertNull(enrollment.getCompletion());
    }
}
