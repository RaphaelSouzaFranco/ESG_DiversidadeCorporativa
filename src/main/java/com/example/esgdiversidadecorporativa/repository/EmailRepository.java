package com.example.esgdiversidadecorporativa.repository;

import com.example.esgdiversidadecorporativa.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

    // Busca e-mails por status (ex: "PENDING", "SENT")
    List<Email> findByStatus(String status);
}
