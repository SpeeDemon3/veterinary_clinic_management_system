package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByClient(OwnerEntity client) throws Exception;
    Optional<List<InvoiceEntity>> findByState(String state) throws Exception;
}
