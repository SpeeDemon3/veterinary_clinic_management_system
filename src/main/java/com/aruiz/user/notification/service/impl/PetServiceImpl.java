package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Pet Service
 *
 * @author Antonio Ruiz
 */
@Service
@Slf4j
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final String[] HEADER = {"ID", "Birthdate", "Description", "Identification Code", "Medication", "Name", "Vaccination Data", "Veterinarian ID"};

    /**
     * Saves a new pet.
     *
     * @param ownerId The ID of the owner of the pet.
     * @param petRequest The details of the pet to be saved.
     * @return The response containing the details of the saved pet.
     * @throws Exception if the owner with the specified ID is not found or there is an error saving the pet.
     */
    @Override
    public PetResponse save(Long ownerId, PetRequest petRequest) throws Exception {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(ownerId);

        if (optionalUserEntity.isPresent()) {

            PetEntity petEntity = modelMapper.map(petRequest, PetEntity.class);

            petEntity.setVeterinarian(optionalUserEntity.get());

            petRepository.save(petEntity);

            log.info("Saving pet entity with ID and NAME -> {} {}", petEntity.getId(), petEntity.getName());

            return modelMapper.map(petEntity, PetResponse.class);

        } else {
            log.error("User with id {} not found!!!!", ownerId);
        }

        log.error("There was an error saving the pet!!!!");
        return null;
    }

    /**
     * Retrieves all pets.
     *
     * @return A list containing the details of all pets.
     * @throws Exception if there are no pets to recover.
     */
    @Override
    public List<PetResponse> findAll() throws Exception {

        List<PetEntity> petEntities = petRepository.findAll();

        if (petEntities.isEmpty()) {
            log.error("There are no entities to recover!!!");
            throw new Exception();
        }

        List<PetResponse> petResponseList = new ArrayList<>();

        for(PetEntity pet : petEntities) {
            log.info("Recovering Pets...");
            petResponseList.add(modelMapper.map(pet, PetResponse.class));
        }

        return petResponseList;
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id The ID of the pet.
     * @return The response containing the details of the retrieved pet.
     * @throws Exception if the pet with the specified ID is not found.
     */
    @Override
    public PetResponse findById(Long id) throws Exception {

        Optional<PetEntity> petEntityOptional = petRepository.findById(id);

        if (petEntityOptional.isPresent()) {

            log.info("Pet with ID " + id + " is present.");

            return modelMapper.map(petEntityOptional.get(), PetResponse.class);

        }

        log.error("Pet with ID -> {} not found!!!", id);
        throw new Exception();
    }

    /**
     * Deletes a pet by its ID.
     *
     * @param id The ID of the pet to delete.
     * @return A message indicating the status of the deletion.
     * @throws Exception if the pet with the specified ID is not found.
     */
    @Override
    public Boolean deleteById(Long id) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(id);

        if (optionalPetEntity.isPresent()) {
            petRepository.deleteById(id);

            log.info("Pet successfully deleted");
            return true;
        }

        log.error("Pet with ID {} not found", id);
        throw new Exception();
    }

    /**
     * Updates a pet by its ID.
     *
     * @param id The ID of the pet.
     * @param petRequestUpdate The updated information of the pet.
     * @return The response containing the updated pet details.
     * @throws Exception if the pet with the specified ID is not found.
     */
    @Override
    public PetResponse updateById(Long id, PetRequestUpdate petRequestUpdate) throws Exception {

        try {

            Optional<PetEntity> optionalPetEntity = petRepository.findById(id);

            if (optionalPetEntity.isPresent()) {

                if (petRequestUpdate.getVeterinarian() == null) {
                    petRequestUpdate.setVeterinarian(optionalPetEntity.get().getVeterinarian());
                }

                if (petRequestUpdate.getIdentificationCode() == null) {
                    petRequestUpdate.setIdentificationCode(optionalPetEntity.get().getIdentificationCode());
                }

                if (petRequestUpdate.getName() == null) {
                    petRequestUpdate.setName(optionalPetEntity.get().getName());
                }

                if (petRequestUpdate.getDescription() == null) {
                    petRequestUpdate.setDescription(optionalPetEntity.get().getDescription());
                }

                if (petRequestUpdate.getVaccinationData() == null) {
                    petRequestUpdate.setVaccinationData(optionalPetEntity.get().getVaccinationData());
                }

                if (petRequestUpdate.getImg() == null) {
                    petRequestUpdate.setImg(optionalPetEntity.get().getImg());
                }

                if (petRequestUpdate.getBirthdate() == null) {
                    petRequestUpdate.setBirthdate(petRequestUpdate.getBirthdate());
                }

                if (petRequestUpdate.getMedication() == null) {
                    petRequestUpdate.setMedication(petRequestUpdate.getMedication());
                }

                PetEntity petEntitySave = modelMapper.map(petRequestUpdate, PetEntity.class);

                petEntitySave.setId(id);

                petRepository.save(petEntitySave);

                log.info("Pet successfully updated!!!");

                return modelMapper.map(petEntitySave, PetResponse.class);
            }

            log.error("Pet with ID {} is not present!!!", id);
            throw new Exception();
        } catch (Exception e) {
            log.error("Error updating pet: {}", e.getMessage(), e);
            // Aquí es donde podrías lanzar una excepción personalizada si lo prefieres
            throw new Exception("Error updating pet: " + e.getMessage(), e);
        }

    }

    /**
     * Adds an image to a pet.
     *
     * @param id The ID of the pet.
     * @param imageFile The image file to be added.
     * @throws IOException if an I/O error occurs while adding the image.
     */
    @Override
    public void addPetImg(Long id, MultipartFile imageFile) throws IOException {

        Optional<PetEntity> petEntityOptional = petRepository.findById(id);

        if (petEntityOptional.isPresent()) {
            log.info("Saving pet image...");

            PetEntity petEntity = petEntityOptional.get();

            petEntity.setImg(Base64.getEncoder().encodeToString(imageFile.getBytes()));

            petRepository.save(petEntity);

        } else {
            throw new IOException();
        }

    }

    /**
     * Retrieves the image of a pet by its ID.
     *
     * @param id The ID of the pet.
     * @return The image byte array.
     * @throws Exception if the pet image cannot be retrieved.
     */
    @Override
    public byte[] getPetImg(Long id) throws Exception {

        PetEntity petEntity = petRepository.findById(id).orElseThrow(RuntimeException::new);

        return Base64.getDecoder().decode(petEntity.getImg());
    }

    /**
     * Generates a CSV file containing information about pets.
     *
     * @return The CSV content as a string.
     * @throws IOException if an I/O error occurs while generating the CSV file.
     */
    @Override
    public String petsInfoDownloadCsv() throws IOException {

        List<PetEntity> petEntityList = petRepository.findAll();

        if (!petEntityList.isEmpty()) {

            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header : HEADER) {
                csvContent.append(header).append(",");

                if (count == HEADER.length) {
                    csvContent.append(header).append("\n");
                }
                count++;
            }

            for (PetEntity pet : petEntityList) {

                csvContent
                        .append(pet.getId()).append(",")
                        .append(pet.getBirthdate()).append(",")
                        .append(pet.getDescription()).append(",")
                        .append(pet.getIdentificationCode()).append(",")
                        .append(pet.getMedication()).append(",")
                        .append(pet.getName()).append(",")
                        .append(pet.getVaccinationData()).append(",")
                        .append(pet.getVeterinarian()).append("\n");
            }

            return csvContent.toString();
        }
        log.error("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}", 0);
        throw new RuntimeException();

    }

    /**
     * Retrieves a pet by its identification code.
     *
     * @param identificationCode The identification code of the pet.
     * @return An Optional containing the pet entity, if found.
     * @throws Exception if the pet with the specified identification code is not found.
     */
    @Override
    public PetResponse findByIdentificationCode(String identificationCode) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findByIdentificationCode(identificationCode);

        if (optionalPetEntity.isPresent()) {
            PetResponse petResponse = modelMapper.map(optionalPetEntity.get(), PetResponse.class);
            return petResponse;
        }

        log.error("Pet with identification code {} not found!!!", identificationCode);

        throw new Exception();
    }

    /**
     * Retrieves information about pets from the database and converts it to JSON format.
     *
     * @return JSON string containing information about pets.
     * @throws Exception if there is an error during the process.
     */
    @Override
    public String petsInfoDownloadJson() throws Exception {

        List<PetEntity> petEntityList = petRepository.findAll();

        if (!petEntityList.isEmpty()) {
            String petsJson = objectMapper.writeValueAsString(petEntityList);

            return petsJson;
        }

        throw new Exception();
    }















}
