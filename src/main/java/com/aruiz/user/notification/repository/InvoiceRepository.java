package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing InvoiceEntity entities.
 * Extends JpaRepository, providing CRUD operations for InvoiceEntity with Long as the primary key type.
 *
 * @author Antonio Ruiz
 */
@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    // Optional<InvoiceEntity> findByClient(OwnerEntity client) throws Exception;
    Optional<List<InvoiceEntity>> findByClientDni(String clientDni) throws Exception;
    Optional<List<InvoiceEntity>> findByState(String state) throws Exception;
}
