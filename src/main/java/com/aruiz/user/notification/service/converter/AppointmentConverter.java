package com.aruiz.user.notification.service.converter;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.controller.dto.UserResponse;
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

@Component
public class AppointmentConverter {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

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

    public AppointmentEntity toAppointmentEntity (AppointmentRequest appointmentRequest) {

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        appointmentEntity.setDateOfAppointment(appointmentRequest.getDateOfAppointment());
        appointmentEntity.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointmentEntity.setDescription(appointmentRequest.getDescription());

        return appointmentEntity;
    }

}
