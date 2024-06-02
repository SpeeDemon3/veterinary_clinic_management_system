package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.domain.Appointment;
import com.aruiz.user.notification.entity.AppointmentEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.AppointmentRepository;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppointmentServiceImp implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String[] HEADER = {"ID", "DATE OF APPOINTMENT", "DESCRIPTION", "VETERINARIAN", "PET"};


    @Override
    public AppointmentResponse save(Long idVeterinarian, Long idPet, AppointmentRequest appointmentRequest) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(idPet);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(idVeterinarian);

        if (optionalPetEntity.isPresent() && optionalUserEntity.isPresent() && appointmentRequest != null) {

            AppointmentEntity appointmentEntity =  modelMapper.map(appointmentRequest, AppointmentEntity.class);

            log.info("Appointment received -> {}", appointmentEntity.toString());

            appointmentEntity.setPet(optionalPetEntity.get());

            appointmentEntity.setVeterinarian(optionalUserEntity.get());

            appointmentRepository.save(appointmentEntity);

            log.info("Saved appointment -> {}", appointmentEntity.toString());


        }

        throw new Exception();

    }

    @Override
    public List<AppointmentEntity> findAll() throws Exception {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {
            return appointmentEntityList;
        }

        throw new Exception();
    }

    @Override
    public AppointmentResponse findById(Long id) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        if (optionalAppointmentEntity.isPresent()) {
            AppointmentEntity appointmentEntity = optionalAppointmentEntity.get();

            log.info("Appointment found successfully -> ID {}", appointmentEntity.getId());

            return modelMapper.map(appointmentEntity, AppointmentResponse.class);
        }

        throw new Exception();
    }

    @Override
    public List<AppointmentResponse> findAppointmentsByPetId(Long idPet) throws Exception {

        Optional<List<AppointmentEntity>> appointmentEntityList = Optional.of(appointmentRepository.findAll());

        Optional<PetEntity> optionalPetEntity = petRepository.findById(idPet);

        if (!appointmentEntityList.isEmpty() && optionalPetEntity.isPresent()) {

            List<AppointmentEntity> appointmentEntities = appointmentEntityList.get();
            PetEntity petEntity = optionalPetEntity.get();
            List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();

            log.info("Recovering appintments with pet ID -> {}", idPet);

            for (AppointmentEntity appointment : appointmentEntities) {

                if (appointment.getPet() == petEntity) {
                    appointmentResponsesList.add(modelMapper.map(appointment, AppointmentResponse.class));
                }

            }

            log.error("Appointments not found for pet ID -> {}", idPet);
            return appointmentResponsesList;

        }


        throw new Exception();
    }

    @Override
    public List<AppointmentResponse> findAppointmentsByVeterinarianId(Long idVeterinarian) throws Exception {

        Optional<List<AppointmentEntity>> appointmentEntityList = Optional.of(appointmentRepository.findAll());

        Optional<UserEntity> optionalUserEntity = userRepository.findById(idVeterinarian);

        if (!appointmentEntityList.isEmpty() && optionalUserEntity.isPresent()) {

            List<AppointmentEntity> appointmentEntities = appointmentEntityList.get();
            UserEntity userEntity = optionalUserEntity.get();
            List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();

            log.info("Recovering appointments with veterinarian ID -> {}", idVeterinarian);

            for (AppointmentEntity appointment : appointmentEntities) {

                if (appointment.getVeterinarian() == userEntity) {
                    appointmentResponsesList.add(modelMapper.map(appointment, AppointmentResponse.class));
                }

            }

            log.error("Appointments not found for veterinarian ID -> {}", idVeterinarian);
            return appointmentResponsesList;

        }

        throw new Exception();
    }

    @Override
    public List<AppointmentResponse> findAppointmentsByDateOfAppointment(String date) throws Exception {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAppointmentsByDateOfAppointment(date);

        if (!appointmentEntityList.isEmpty()) {
            List<AppointmentResponse> appointmentResponses = new ArrayList<>();

            for (AppointmentEntity appointment : appointmentEntityList) {
                appointmentResponses.add(modelMapper.map(appointment, AppointmentResponse.class));
            }

            log.info("Recovering appointments...");
            return appointmentResponses;
        }
        log.error("Apointment not found with DATE -> {}", date);
        throw new Exception();
    }

    @Override
    public String deleteById(Long id) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        if (optionalAppointmentEntity.isPresent()) {
            appointmentRepository.deleteById(id);

            return "Apointment successfully deleted -> ID: " + id;
        }

        throw new Exception();
    }

    @Override
    public AppointmentResponse updateById(Long id, AppointmentRequestUpdate appointmentRequestUpdate) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        if (optionalAppointmentEntity.isPresent()) {

            AppointmentEntity appointmentEntityfound = optionalAppointmentEntity.get();

            AppointmentEntity appointmentEntitysave = new AppointmentEntity();

            if (appointmentEntityfound.getDateOfAppointment() == null) {
                appointmentEntitysave.setAppointmentTime(appointmentEntityfound.getDateOfAppointment());
            }

            if (appointmentEntityfound.getAppointmentTime() == null) {
                appointmentEntitysave.setAppointmentTime(appointmentEntityfound.getAppointmentTime());
            }

            if (appointmentEntityfound.getDescription() == null) {
                appointmentEntitysave.setDescription(appointmentEntityfound.getDescription());
            }

            if (appointmentEntityfound.getVeterinarian() == null) {
                appointmentEntitysave.setVeterinarian(appointmentEntityfound.getVeterinarian());
            }

            if (appointmentEntityfound.getPet() == null) {
                appointmentEntitysave.setPet(appointmentEntityfound.getPet());
            }

            appointmentEntitysave = appointmentEntityfound;

            appointmentEntitysave.setId(id);

            appointmentRepository.save(appointmentEntitysave);

            log.info("Appointment updated successfully: ID -> {}", id);

            return modelMapper.map(appointmentEntitysave, AppointmentResponse.class);

        }

        throw new Exception();
    }

    @Override
    public String appointmentInfoDownloadCsv() throws IOException {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {

            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header : HEADER) {
                csvContent.append(header).append(",");

                if (count == HEADER.length) {

                    csvContent.append(header).append("\n");

                }
                count++;
            }
            //"ID", "DATE OF APPOINTMENT", "DESCRIPTION", "VETERINARIAN", "PET"

            for (AppointmentEntity appointment : appointmentEntityList) {
                csvContent
                        .append(appointment.getId()).append(",")
                        .append(appointment.getDateOfAppointment()).append(",")
                        .append(appointment.getDescription()).append(",")
                        .append(appointment.getVeterinarian()).append(",")
                        .append(appointment.getPet()).append("\n");
            }
            return csvContent.toString();
        }

        log.error("There aren't entities in database!!!!!!!!!!!!!! Numer entities= {}", 0);
        throw new RuntimeException();
    }

    @Override
    public String appointmentInfoDownloadJson() throws IOException {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {
            String apointmentsJson = objectMapper.writeValueAsString(appointmentEntityList);

            return apointmentsJson;
        }

        throw new RuntimeException();
    }
}
