package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.entity.AppointmentEntity;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for managing appointments.
 * Defines methods for appointment-related operations.
 *
 * @author Antonio Ruiz
 */
public interface AppointmentService {

    AppointmentResponse save (Long idVeterinarian, Long idPet, AppointmentRequest appointmentRequest) throws Exception;

    List<AppointmentResponse> findAll () throws Exception;

    AppointmentResponse findById (Long id) throws Exception;

    List<AppointmentResponse> findAppointmentsByPetId (Long idPet) throws Exception;

    List<AppointmentResponse> findAppointmentsByVeterinarianId (Long idVeterinarian) throws Exception;

    List<AppointmentResponse> findAppointmentsByDateOfAppointment (String dateOfAppointment) throws Exception;

    Boolean deleteById (Long id) throws Exception;

    AppointmentResponse updateById (Long id, AppointmentRequestUpdate appointmentRequestUpdate) throws Exception;

    String appointmentInfoDownloadCsv() throws IOException;

    String appointmentInfoDownloadJson() throws IOException;


}
