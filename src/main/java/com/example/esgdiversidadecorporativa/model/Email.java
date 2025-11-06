package com.example.esgdiversidadecorporativa.model;

import jakarta.persistence.*;
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
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_outbox_seq")
    @SequenceGenerator(name = "email_outbox_seq", sequenceName = "email_outbox_seq", allocationSize = 1)
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Destinatário é obrigatório")
    @jakarta.validation.constraints.Email(message = "Destinatário deve ser um email válido")
    @Column(name = "recipient", nullable = false, length = 150)
    private String recipient;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(min = 5, max = 200, message = "Assunto deve ter entre 5 e 200 caracteres")
    @Column(name = "subject", nullable = false, length = 200)
    private String subject;

    @NotBlank(message = "Corpo da mensagem é obrigatório")
    @Column(name = "body", nullable = false, columnDefinition = "CLOB")
    private String body;

    @NotBlank(message = "Status é obrigatório")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
    }

    public void markAsSent() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = "FAILED";
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isSent() {
        return "SENT".equalsIgnoreCase(status);
    }
}
