package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_outbox_seq")
    @SequenceGenerator(name = "email_outbox_seq", sequenceName = "email_outbox_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Destinatário é obrigatório")
    @Email(message = "Destinatário deve ser um email válido")
    @Column(name = "recipient", nullable = false, length = 150)
    private String recipient;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(min = 5, max = 200, message = "Assunto deve ter entre 5 e 200 caracteres")
    @Column(name = "subject", nullable = false, length = 200)
    private String subject;

    @NotBlank(message = "Corpo da mensagem é obrigatório")
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @NotBlank(message = "Status é obrigatório")
    @Column(name = "status", nullable = false, length = 20)
    private String status; // "PENDING", "SENT", "FAILED"

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    //  executado antes de persistir a entidade
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
    }

    //  auxiliar para marcar como enviado
    public void markAsSent() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }

    //  auxiliar para marcar como falha
    public void markAsFailed() {
        this.status = "FAILED";
    }

    //  auxiliar para verificar se está pendente
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    //  auxiliar para verificar se foi enviado
    public boolean isSent() {
        return "SENT".equalsIgnoreCase(status);
    }
}