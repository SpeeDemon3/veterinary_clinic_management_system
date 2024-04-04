package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<List<RoleEntity>> findAllByName(String name) throws Exception;

    Optional<RoleEntity> findByName(String name) throws Exception;

}
