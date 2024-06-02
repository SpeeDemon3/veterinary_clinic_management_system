package com.aruiz.user.notification.repository;

import com.aruiz.user.notification.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findAppointmentsByDateOfAppointment(String dateOfAppointment);
}
