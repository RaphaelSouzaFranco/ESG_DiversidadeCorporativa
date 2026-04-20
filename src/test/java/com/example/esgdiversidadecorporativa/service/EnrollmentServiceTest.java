package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.entity.Department;
import com.example.esgdiversidadecorporativa.entity.Employee;
import com.example.esgdiversidadecorporativa.entity.Enrollment;
import com.example.esgdiversidadecorporativa.entity.Training;
import com.example.esgdiversidadecorporativa.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Employee employee;
    private Training training;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        Department department = new Department();
        department.setDepartmentId("DEPT-001");
        department.setName("RH");

        employee = new Employee();
        employee.setEmployeeId("EMP-001");
        employee.setName("Nicolly");
        employee.setEmail("nicolly@empresa.com");
        employee.setGender("F");
        employee.setDepartment(department);
        employee.setEnrollments(new ArrayList<>());

        training = new Training();
        training.setTrainingId("TRN-001");
        training.setTitle("Diversidade e Inclusão");
        training.setDueDate(LocalDate.now().plusDays(30));
        training.setEnrollments(new ArrayList<>());

        enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR_001");
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setEmployee(employee);
        enrollment.setTraining(training);
    }

    // -------------------------------------------------------
    // findAll
    // -------------------------------------------------------

    @Test
    void deveRetornarTodasAsMatriculas() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.findAll();

        assertEquals(1, result.size());
        assertEquals("ENR_001", result.get(0).getEnrollmentId());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaMatriculas() {
        when(enrollmentRepository.findAll()).thenReturn(List.of());

        List<Enrollment> result = enrollmentService.findAll();

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------
    // findById
    // -------------------------------------------------------

    @Test
    void deveRetornarMatriculaPorId() {
        when(enrollmentRepository.findById("ENR_001")).thenReturn(Optional.of(enrollment));

        Optional<Enrollment> result = enrollmentService.findById("ENR_001");

        assertTrue(result.isPresent());
        assertEquals("ENR_001", result.get().getEnrollmentId());
    }

    @Test
    void deveRetornarVazioQuandoMatriculaNaoEncontrada() {
        when(enrollmentRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        Optional<Enrollment> result = enrollmentService.findById("NAO-EXISTE");

        assertFalse(result.isPresent());
    }

    // -------------------------------------------------------
    // save
    // -------------------------------------------------------

    @Test
    void deveSalvarMatriculaComSucesso() {
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.save(enrollment);

        assertNotNull(result);
        assertEquals("ENR_001", result.getEnrollmentId());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void deveSalvarMatriculaSemIdEGerarAutomaticamente() {
        Enrollment semId = new Enrollment();
        semId.setEnrollmentDate(LocalDate.now());
        semId.setEmployee(employee);
        semId.setTraining(training);
        // ID gerado via @PrePersist, simulamos o save retornando com ID
        semId.setEnrollmentId("ENR_GERADO");

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(semId);

        Enrollment result = enrollmentService.save(semId);

        assertNotNull(result.getEnrollmentId());
    }

    // -------------------------------------------------------
    // deleteById
    // -------------------------------------------------------

    @Test
    void deveDeletarMatriculaPorId() {
        doNothing().when(enrollmentRepository).deleteById("ENR_001");

        assertDoesNotThrow(() -> enrollmentService.deleteById("ENR_001"));

        verify(enrollmentRepository, times(1)).deleteById("ENR_001");
    }

    // -------------------------------------------------------
    // Regras de negócio da entidade Enrollment
    // -------------------------------------------------------

    @Test
    void matriculaSemConclusaoNaoEstaCompleta() {
        enrollment.setCompletion(null);

        assertFalse(enrollment.isCompleted());
    }

    @Test
    void matriculaSemNotificacaoPrevisaNecessitaNotificacao() {
        enrollment.setCompletion(null);
        enrollment.setLastNotificationDate(null);

        assertTrue(enrollment.needsNotification());
    }

    @Test
    void matriculaComNotificacaoRecenteNaoNecessitaNotificacao() {
        enrollment.setCompletion(null);
        enrollment.setLastNotificationDate(LocalDate.now());

        assertFalse(enrollment.needsNotification());
    }

    @Test
    void matriculaComNotificacaoAntigaNecessitaNotificacao() {
        enrollment.setCompletion(null);
        enrollment.setLastNotificationDate(LocalDate.now().minusDays(8));

        assertTrue(enrollment.needsNotification());
    }

    @Test
    void matriculaConcluiidaNaoNecessitaNotificacao() {
        com.example.esgdiversidadecorporativa.entity.Completion completion =
                new com.example.esgdiversidadecorporativa.entity.Completion();
        enrollment.setCompletion(completion);

        assertFalse(enrollment.needsNotification());
    }
}