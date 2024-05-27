package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    Optional<PetEntity> findByIdentificationCode(String identificationCode) throws Exception;

}
