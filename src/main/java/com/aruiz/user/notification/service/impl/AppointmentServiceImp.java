package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.controller.dto.AppointmentResponse;
import com.aruiz.user.notification.entity.AppointmentEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.AppointmentRepository;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.AppointmentService;
import com.aruiz.user.notification.service.converter.AppointmentConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implement Appointment Service
 *
 * @author Antonio Ruiz = speedemon
 */
@Service
@Slf4j
public class AppointmentServiceImp implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppointmentConverter appointmentConverter;

    private final String[] HEADER = {"ID", "DATE OF APPOINTMENT", "DESCRIPTION", "VETERINARIAN", "PET"};

    /**
     * Creates a new appointment for a pet with a veterinarian.
     *
     * @param idVeterinarian  The ID of the veterinarian assigned to the appointment.
     * @param idPet           The ID of the pet involved in the appointment.
     * @param appointmentRequest The appointment details.
     * @return The created {@link AppointmentResponse}.
     * @throws Exception If there's an issue with creating the appointment.
     */
    @Override
    public AppointmentResponse save(Long idVeterinarian, Long idPet, AppointmentRequest appointmentRequest) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(idPet);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(idVeterinarian);

        if (optionalPetEntity.isPresent() && optionalUserEntity.isPresent() && appointmentRequest != null) {

            AppointmentEntity appointmentEntity =  appointmentConverter.toAppointmentEntity(appointmentRequest);

            log.info("Appointment received -> {}", appointmentEntity.toString());

            appointmentEntity.setPet(optionalPetEntity.get());

            appointmentEntity.setVeterinarian(optionalUserEntity.get());

            appointmentRepository.save(appointmentEntity);

            log.info("Saved appointment -> {}", appointmentEntity.toString());

            return appointmentConverter.toAppointmentResponse(appointmentEntity);
        }

        throw new Exception();

    }

    /**
     * Retrieves all appointments from the database.
     *
     * @return A list of {@link AppointmentEntity} representing all appointments.
     * @throws Exception If no appointments are found in the database.
     */
    @Override
    public List<AppointmentEntity> findAll() throws Exception {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {
            return appointmentEntityList;
        }

        throw new Exception();
    }

    /**
     * Finds an appointment by its ID.
     *
     * @param id The ID of the appointment to find.
     * @return The {@link AppointmentResponse} representing the appointment found.
     * @throws Exception If no appointment is found with the specified ID.
     */
    @Override
    public AppointmentResponse findById(Long id) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        if (optionalAppointmentEntity.isPresent()) {
            AppointmentEntity appointmentEntity = optionalAppointmentEntity.get();

            log.info("Appointment found successfully -> ID {}", appointmentEntity.getId());

            return appointmentConverter.toAppointmentResponse(appointmentEntity);
        }

        throw new Exception();
    }

    /**
     * Finds appointments associated with a pet based on pet ID.
     *
     * @param idPet The ID of the pet to search appointments for.
     * @return A list of {@link AppointmentResponse} containing appointments associated with the pet.
     * @throws Exception If no pet is found with the specified ID or if no appointments are found for the pet.
     */
    @Override
    public List<AppointmentResponse> findAppointmentsByPetId(Long idPet) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(idPet);

        if (optionalPetEntity.isPresent()) {

            PetEntity petEntity = optionalPetEntity.get();
            log.info("Pet found with ID -> {}", optionalPetEntity.get().getId());

            List<AppointmentEntity> appointmentEntities = appointmentRepository.findAll();
            List<AppointmentResponse> appointmentResponsesList = new ArrayList<>();

            log.info("Recovering appintments with pet ID -> {}", idPet);

            for (AppointmentEntity appointment : appointmentEntities) {

                if (appointment.getPet().equals(petEntity)) {
                    log.info(appointment.toString());
                    appointmentResponsesList.add(appointmentConverter.toAppointmentResponse(appointment));
                }

            }

            return appointmentResponsesList;

        }
        log.error("Pet not found with ID -> {}", idPet);
        throw new Exception("Pet not found");
    }

    /**
     * Finds appointments assigned to a veterinarian based on veterinarian ID.
     *
     * @param idVeterinarian The ID of the veterinarian to search appointments for.
     * @return A list of {@link AppointmentResponse} containing appointments assigned to the veterinarian.
     * @throws Exception If no appointments are found for the specified veterinarian ID.
     */
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
                    appointmentResponsesList.add(appointmentConverter.toAppointmentResponse(appointment));
                }

            }

            log.error("Appointments not found for veterinarian ID -> {}", idVeterinarian);
            return appointmentResponsesList;

        }

        throw new Exception();
    }

    /**
     * Finds appointments by the date of appointment.
     *
     * @param date The date of appointment to search for in "yyyy-MM-dd" format.
     * @return A list of {@link AppointmentResponse} containing appointments found for the specified date.
     * @throws Exception If no appointments are found for the specified date.
     */
    @Override
    public List<AppointmentResponse> findAppointmentsByDateOfAppointment(String date) throws Exception {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAppointmentsByDateOfAppointment(date);

        if (!appointmentEntityList.isEmpty()) {
            List<AppointmentResponse> appointmentResponses = new ArrayList<>();

            for (AppointmentEntity appointment : appointmentEntityList) {
                appointmentResponses.add(appointmentConverter.toAppointmentResponse(appointment));
            }

            log.info("Recovering appointments...");
            return appointmentResponses;
        }
        log.error("Apointment not found with DATE -> {}", date);
        throw new Exception();
    }

    /**
     * Deletes an appointment by its ID.
     *
     * @param id The ID of the appointment to delete.
     * @return A message indicating the success of the deletion.
     * @throws Exception If the appointment with the specified ID is not found.
     */
    @Override
    public Boolean deleteById(Long id) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        if (optionalAppointmentEntity.isPresent()) {
            appointmentRepository.deleteById(id);
            log.info("Apointment successfully deleted -> ID: {}", id);
            return true;
        }

        throw new Exception();
    }

    /**
     * Updates an appointment by its ID with the details provided in the {@code appointmentRequestUpdate}.
     *
     * @param id The ID of the appointment to update.
     * @param appointmentRequestUpdate The updated appointment details.
     * @return The updated appointment response.
     * @throws Exception If the appointment with the specified ID is not found or if there's an error during update.
     */
    @Override
    public AppointmentResponse updateById(Long id, AppointmentRequestUpdate appointmentRequestUpdate) throws Exception {

        Optional<AppointmentEntity> optionalAppointmentEntity = appointmentRepository.findById(id);

        log.info("appointmentRequestUpdate value -> {}", appointmentRequestUpdate.toString());

        if (optionalAppointmentEntity.isPresent()) {

            log.info("ID found {}", id);

            AppointmentEntity appointmentEntityFound = optionalAppointmentEntity.get();

            log.info("AppointmentEntityFound -> {}", appointmentEntityFound.toString());

            AppointmentEntity appointmentEntitysave = new AppointmentEntity();

            if (appointmentRequestUpdate.getDateOfAppointment() == null) {
                appointmentEntitysave.setDateOfAppointment(appointmentEntityFound.getDateOfAppointment());
            } else {
                appointmentEntitysave.setDateOfAppointment(appointmentRequestUpdate.getDateOfAppointment());
            }

            if (appointmentRequestUpdate.getAppointmentTime() == null) {
                appointmentEntitysave.setAppointmentTime(appointmentEntityFound.getAppointmentTime());
            } else {
                log.info("Value timne: {}", appointmentRequestUpdate.getAppointmentTime());
                appointmentEntitysave.setAppointmentTime(appointmentRequestUpdate.getAppointmentTime());
                log.info("Value time entity found: {}", appointmentEntitysave.getAppointmentTime());
            }

            if (appointmentRequestUpdate.getDescription() == null) {
                appointmentEntitysave.setDescription(appointmentEntityFound.getDescription());
            } else {
                appointmentEntitysave.setDescription(appointmentRequestUpdate.getDescription());
            }

            if (appointmentRequestUpdate.getVeterinarian() == null) {
                appointmentEntitysave.setVeterinarian(appointmentEntityFound.getVeterinarian());
            } else {
                appointmentEntitysave.setVeterinarian(modelMapper.map(appointmentRequestUpdate.getVeterinarian(), UserEntity.class));
            }

            if (appointmentRequestUpdate.getPet() == null) {
                appointmentEntitysave.setPet(appointmentEntityFound.getPet());
            } else {
                appointmentEntitysave.setPet(modelMapper.map(appointmentRequestUpdate.getPet(), PetEntity.class));
            }


            appointmentEntitysave.setId(id);

            appointmentRepository.save(appointmentEntitysave);

            log.info(appointmentEntitysave.toString());

            log.info("Appointment updated successfully: ID -> {}", id);

            return appointmentConverter.toAppointmentResponse(appointmentEntitysave);

        }

        throw new Exception("Appointment not found!!!!");
    }


    /**
     * Retrieves all appointment information and returns it in CSV format.
     *
     * @return CSV content containing all appointment information.
     * @throws IOException if an error occurs during CSV content creation.
     * @throws RuntimeException if no appointments are found in the database.
     */
    @Override
    public String appointmentInfoDownloadCsv() throws IOException {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {
            // Prepare StringBuilder to hold CSV content
            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header : HEADER) {
                csvContent.append(header).append(",");

                if (count == HEADER.length) {

                    csvContent.append(header).append("\n");

                }
                count++;
            }

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

        log.error("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}", 0);
        throw new RuntimeException();
    }

    /**
     * Retrieves all appointment information and returns it in JSON format.
     *
     * @return JSON string containing all appointment information.
     * @throws IOException if an error occurs during JSON processing.
     * @throws RuntimeException if no appointments are found in the database.
     */
    @Override
    public String appointmentInfoDownloadJson() throws IOException {

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAll();

        if (!appointmentEntityList.isEmpty()) {
            // Convert the list of appointment entities to a JSON string
            String apointmentsJson = objectMapper.writeValueAsString(appointmentEntityList);

            return apointmentsJson;
        }

        throw new RuntimeException();
    }

}
