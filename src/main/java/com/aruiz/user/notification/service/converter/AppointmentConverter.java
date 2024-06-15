package com.aruiz.user.notification.service.converter;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.domain.Pet;
import com.aruiz.user.notification.domain.User;
import com.aruiz.user.notification.entity.AppointmentEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.service.PetService;
import com.aruiz.user.notification.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converter Appointment
 *
 * @author Antonio Ruiz = speedemon
 */
@Component
public class AppointmentConverter {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converts an AppointmentEntity to an AppointmentResponse.
     *
     * @param appointmentEntity The AppointmentEntity object to convert.
     * @return An AppointmentResponse object mapped from the AppointmentEntity.
     * @throws Exception If there is an error while fetching user or pet information.
     */
    public AppointmentResponse toAppointmentResponse (AppointmentEntity appointmentEntity) throws Exception {

        AppointmentResponse appointmentResponse = new AppointmentResponse();
        UserResponse userResponse = userService.findById(appointmentEntity.getVeterinarian().getId());
        PetResponse petResponse = petService.findById(appointmentEntity.getPet().getId());


        appointmentResponse.setId(appointmentEntity.getId());
        appointmentResponse.setDateOfAppointment(appointmentEntity.getDateOfAppointment());
        appointmentResponse.setDescription(appointmentEntity.getDescription());
        appointmentResponse.setVeterinarian(modelMapper.map(userResponse, User.class));
        appointmentResponse.setPet(modelMapper.map(petResponse, Pet.class));

        return appointmentResponse;
    }

    /**
     * Converts an AppointmentResponse object to an AppointmentEntity.
     *
     * @param appointmentResponse The AppointmentResponse object containing response appointment information.
     * @return An AppointmentEntity object mapped from the AppointmentResponse.
     */
    public AppointmentEntity toAppointmentEntity (AppointmentResponse appointmentResponse) {

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        User user = appointmentResponse.getVeterinarian();
        Pet pet = appointmentResponse.getPet();

        appointmentEntity.setId(appointmentResponse.getId());
        appointmentEntity.setDateOfAppointment(appointmentResponse.getDateOfAppointment());
        appointmentEntity.setAppointmentTime(appointmentEntity.getAppointmentTime());
        appointmentEntity.setDescription(appointmentEntity.getDescription());
        appointmentEntity.setVeterinarian(modelMapper.map(user, UserEntity.class));
        appointmentEntity.setPet(modelMapper.map(pet, PetEntity.class));

        return appointmentEntity;
    }

    /**
     * Converts an AppointmentRequest object to an AppointmentEntity.
     *
     * @param appointmentRequest The AppointmentRequest object containing request appointment information.
     * @return An AppointmentEntity object mapped from the AppointmentRequest.
     */
    public AppointmentEntity toAppointmentEntity (AppointmentRequest appointmentRequest) {

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        appointmentEntity.setDateOfAppointment(appointmentRequest.getDateOfAppointment());
        appointmentEntity.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointmentEntity.setDescription(appointmentRequest.getDescription());

        return appointmentEntity;
    }

    /**
     * Converts an AppointmentRequestUpdate object to an AppointmentEntity.
     *
     * @param appointmentRequestUpdate The AppointmentRequestUpdate object containing updated appointment information.
     * @return An AppointmentEntity object mapped from the AppointmentRequestUpdate.
     * @throws IllegalArgumentException If the veterinarian or pet with the specified ID is not found.
     */
    public AppointmentEntity toAppointmentEntity (AppointmentRequestUpdate appointmentRequestUpdate) {

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        // Set basic appointment details
        appointmentEntity.setDateOfAppointment(appointmentRequestUpdate.getDateOfAppointment());
        appointmentEntity.setAppointmentTime(appointmentRequestUpdate.getAppointmentTime());
        appointmentEntity.setDescription(appointmentRequestUpdate.getDescription());

        // Set veterinarian if provided
        if (appointmentRequestUpdate.getVeterinarian().getId() > 0) {
            UserEntity veterinarian = modelMapper.map(appointmentRequestUpdate.getVeterinarian(), UserEntity.class);
            appointmentEntity.setVeterinarian(veterinarian);
        }

        // Set pet if provided
        if (appointmentRequestUpdate.getPet().getId() > 0) {
            PetEntity petEntity = modelMapper.map(appointmentRequestUpdate.getPet(), PetEntity.class);
            appointmentEntity.setPet(petEntity);
        }

        return appointmentEntity;

    }

}
