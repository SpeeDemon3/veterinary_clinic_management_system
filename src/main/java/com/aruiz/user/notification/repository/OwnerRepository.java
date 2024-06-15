package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Owner Repository extends JpaRepository
 *
 * @author Antonio Ruiz = speedemon
 */
@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    Optional<OwnerEntity> findByEmail(String email) throws Exception;

    Optional<OwnerEntity> findByDni (String dni) throws Exception;
}
