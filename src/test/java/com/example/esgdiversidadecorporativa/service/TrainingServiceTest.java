package com.example.esgdiversidadecorporativa.service;

import com.example.esgdiversidadecorporativa.entity.Enrollment;
import com.example.esgdiversidadecorporativa.entity.Training;
import com.example.esgdiversidadecorporativa.repository.TrainingRepository;
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
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setTrainingId("TRN-001");
        training.setTitle("Diversidade e Inclusão");
        training.setDescription("Treinamento sobre diversidade corporativa.");
        training.setDueDate(LocalDate.now().plusDays(30));
        training.setEnrollments(new ArrayList<>());
    }

    // -------------------------------------------------------
    // findAll
    // -------------------------------------------------------

    @Test
    void deveRetornarTodosOsTreinamentos() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.findAll();

        assertEquals(1, result.size());
        assertEquals("Diversidade e Inclusão", result.get(0).getTitle());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaTreinamentos() {
        when(trainingRepository.findAll()).thenReturn(List.of());

        List<Training> result = trainingService.findAll();

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------
    // findById
    // -------------------------------------------------------

    @Test
    void deveRetornarTreinamentoPorId() {
        when(trainingRepository.findById("TRN-001")).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.findById("TRN-001");

        assertTrue(result.isPresent());
        assertEquals("TRN-001", result.get().getTrainingId());
    }

    @Test
    void deveRetornarVazioQuandoTreinamentoNaoEncontrado() {
        when(trainingRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        Optional<Training> result = trainingService.findById("NAO-EXISTE");

        assertFalse(result.isPresent());
    }

    // -------------------------------------------------------
    // save
    // -------------------------------------------------------

    @Test
    void deveSalvarTreinamentoComSucesso() {
        // ID já definido — pula geração automática
        // Título novo — não existe ainda
        when(trainingRepository.existsByTitle("Diversidade e Inclusão")).thenReturn(false);
        // Não está vencido (dueDate futuro)
        // findById retorna empty (não é edição de existente)
        when(trainingRepository.findById("TRN-001")).thenReturn(Optional.empty());
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        Training result = trainingService.save(training);

        assertNotNull(result);
        assertEquals("TRN-001", result.getTrainingId());
        verify(trainingRepository).save(training);
    }

    @Test
    void deveGerarIdAutomaticoQuandoNaoInformado() {
        training.setTrainingId(null);
        when(trainingRepository.existsByTitle(anyString())).thenReturn(false);
        when(trainingRepository.save(any(Training.class))).thenAnswer(inv -> inv.getArgument(0));

        Training result = trainingService.save(training);

        assertNotNull(result.getTrainingId());
        assertTrue(result.getTrainingId().startsWith("TRN-"));
    }

    @Test
    void deveLancarExcecaoQuandoTituloJaExiste() {
        when(trainingRepository.existsByTitle("Diversidade e Inclusão")).thenReturn(true);
        when(trainingRepository.findById(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> trainingService.save(training));

        assertTrue(ex.getMessage().contains("Diversidade e Inclusão"));
    }

    @Test
    void deveLancarExcecaoQuandoDueDateEstaNoPassado() {
        training.setDueDate(LocalDate.now().minusDays(1));
        when(trainingRepository.existsByTitle(anyString())).thenReturn(false);
        when(trainingRepository.findById(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> trainingService.save(training));

        assertEquals("A data de conclusão (dueDate) não pode estar no passado.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoAoEditarTreinamentoVencido() {
        Training vencido = new Training();
        vencido.setTrainingId("TRN-001");
        vencido.setTitle("Antigo");
        vencido.setDueDate(LocalDate.now().minusDays(5)); // vencido
        vencido.setEnrollments(new ArrayList<>());

        when(trainingRepository.findById("TRN-001")).thenReturn(Optional.of(vencido));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> trainingService.save(training));

        assertEquals("Não é possível alterar um treinamento cuja data já expirou.", ex.getMessage());
    }

    // -------------------------------------------------------
    // deleteById
    // -------------------------------------------------------

    @Test
    void deveDeletarTreinamentoSemMatriculas() {
        when(trainingRepository.findById("TRN-001")).thenReturn(Optional.of(training));
        doNothing().when(trainingRepository).deleteById("TRN-001");

        assertDoesNotThrow(() -> trainingService.deleteById("TRN-001"));

        verify(trainingRepository).deleteById("TRN-001");
    }

    @Test
    void deveLancarExcecaoAoDeletarTreinamentoInexistente() {
        when(trainingRepository.findById("NAO-EXISTE")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> trainingService.deleteById("NAO-EXISTE"));

        assertEquals("Treinamento não encontrado: NAO-EXISTE", ex.getMessage());
        verify(trainingRepository, never()).deleteById(any());
    }

    @Test
    void deveLancarExcecaoAoDeletarTreinamentoComMatriculas() {
        Enrollment enrollment = new Enrollment();
        training.getEnrollments().add(enrollment);

        when(trainingRepository.findById("TRN-001")).thenReturn(Optional.of(training));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> trainingService.deleteById("TRN-001"));

        assertEquals("Não é possível excluir um treinamento com matrículas associadas.", ex.getMessage());
        verify(trainingRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------
    // Regras da entidade Training
    // -------------------------------------------------------

    @Test
    void treinamentoComDueDatePassadaEstaVencido() {
        training.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(training.isOverdue());
    }

    @Test
    void treinamentoComDueDateFuturaENaoEstaVencido() {
        training.setDueDate(LocalDate.now().plusDays(1));
        assertFalse(training.isOverdue());
    }
}