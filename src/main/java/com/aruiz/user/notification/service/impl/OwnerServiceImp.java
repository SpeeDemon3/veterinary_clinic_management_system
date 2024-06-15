package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.repository.OwnerRepository;
import com.aruiz.user.notification.service.OwnerService;
import com.aruiz.user.notification.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implement Owner Service
 *
 * @author Antonio Ruiz
 */
@Service
@Slf4j
public class OwnerServiceImp implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final String[] HEADERS = {"ID", "DNI", "EMAIL", "NAME", "LAST_NAME", "Phone_Number"};

    /**
     * Saves a new owner with the provided details and associates them with a pet identified by petId.
     *
     * @param petId        The ID of the pet to associate with the new owner.
     * @param ownerRequest The request object containing details of the owner to be saved.
     * @return The response object representing the saved owner.
     * @throws Exception Throws an exception if there are issues saving the owner or if the pet with the specified ID does not exist.
     */
    @Override
    public OwnerResponse save(Long petId, OwnerRequest ownerRequest) throws Exception {

        Optional<PetResponse> optionalPet = Optional.ofNullable(petService.findById(petId));

        if (ownerRequest != null && optionalPet != null) {

            List<PetEntity> petEntityList = new ArrayList<>();
            PetEntity petEntity = modelMapper.map(optionalPet.get(), PetEntity.class);
            petEntityList.add(petEntity);

            OwnerEntity ownerEntity = modelMapper.map(ownerRequest, OwnerEntity.class);

            ownerEntity.setPets(petEntityList);
            petEntity.setOwner(ownerEntity);

            petService.updateById(petId, modelMapper.map(petEntity, PetRequestUpdate.class));

            ownerRepository.save(ownerEntity);
            log.info("Owner created successfully!!! -> {}", ownerEntity.toString());

            return modelMapper.map(ownerEntity, OwnerResponse.class);
        }

        throw new Exception("Something went wrong!!!!");
    }

    /**
     * Retrieves all owners from the database.
     *
     * @return A list of owner responses containing details of all owners found.
     * @throws Exception Throws an exception if no owners are found in the database.
     */
    @Override
    public List<OwnerResponse> findAll() throws Exception {

        Optional<List<OwnerEntity>> optionalOwnerEntity = Optional.of(ownerRepository.findAll());

        if (!optionalOwnerEntity.isEmpty()) {
            List<OwnerEntity> ownerEntityList = optionalOwnerEntity.get();
            List<OwnerResponse> ownerResponseList = new ArrayList<>();

            for (OwnerEntity owner : ownerEntityList) {
                ownerResponseList.add(modelMapper.map(owner, OwnerResponse.class));
            }

            return ownerResponseList;

        }

        throw new Exception("List owners is empty!!!! SIZE -> " + optionalOwnerEntity.get().size());
    }

    /**
     * Retrieves an owner from the database based on the provided ID.
     *
     * @param id The ID of the owner to retrieve.
     * @return The owner response containing details of the owner found.
     * @throws Exception Throws an exception if the owner with the specified ID is not found.
     */
    @Override
    public OwnerResponse findById(Long id) throws Exception {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(id);

        if (optionalOwnerEntity.isPresent()) {
            return modelMapper.map(optionalOwnerEntity.get(), OwnerResponse.class);
        }

        throw new Exception();
    }

    /**
     * Deletes an owner from the database based on the provided ID.
     *
     * @param id The ID of the owner to delete.
     * @return A message indicating the successful deletion of the owner.
     * @throws Exception Throws an exception if the owner with the specified ID is not found.
     */
    @Override
    public String deleteById(Long id) throws Exception {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(id);

        if (optionalOwnerEntity.isPresent()) {
            ownerRepository.deleteById(id);
            return "Owner with ID -> " + id + " successfully deleted!!!!";
        }

        throw new Exception();
    }

    /**
     * Updates an owner's information based on the provided ID.
     *
     * @param id The ID of the owner to update.
     * @param ownerRequest The OwnerRequest object containing the updated information.
     * @return An OwnerResponse object representing the updated owner.
     * @throws Exception Throws an exception if the owner with the specified ID is not found.
     */
    @Override
    public OwnerResponse updateById(Long id, OwnerRequest ownerRequest) throws Exception {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(id);

        if (optionalOwnerEntity.isPresent()) {

            OwnerEntity ownerEntitySave = new OwnerEntity();

            if (ownerRequest.getName() == null) {
                ownerEntitySave.setName(optionalOwnerEntity.get().getName());
            } else {
                ownerEntitySave.setName(ownerRequest.getName());
            }

            if (ownerRequest.getLastName() == null) {
                ownerEntitySave.setLastName(optionalOwnerEntity.get().getLastName());
            } else {
                ownerEntitySave.setLastName(ownerRequest.getLastName());
            }

            if (ownerRequest.getEmail() == null) {
                ownerEntitySave.setEmail(optionalOwnerEntity.get().getEmail());
            } else {
                ownerEntitySave.setEmail(ownerRequest.getEmail());
            }

            if (ownerRequest.getDni() == null) {
                ownerEntitySave.setDni(optionalOwnerEntity.get().getDni());
            } else {
                ownerEntitySave.setDni(ownerRequest.getDni());
            }

            if (ownerRequest.getPhoneNumber() == null) {
                ownerEntitySave.setPhoneNumber(optionalOwnerEntity.get().getPhoneNumber());
            } else {
                ownerEntitySave.setPhoneNumber(ownerRequest.getPhoneNumber());
            }

            List<PetEntity> petEntityList = ownerRepository.findById(id).get().getPets();

            ownerEntitySave.setPets(petEntityList);

            ownerEntitySave.setId(id);

            ownerRepository.save(ownerEntitySave);

            return modelMapper.map(ownerEntitySave, OwnerResponse.class);

        }

        throw new Exception("Owner not found!!!");
    }

    /**
     * Retrieves information about owners in CSV format.
     *
     * @return A CSV-formatted string representing information about owners.
     * @throws Exception Throws an exception if no owner entities are found in the database.
     */
    @Override
    public String ownerInfoDownloadCsv() throws Exception {

        List<OwnerEntity> ownerEntityList = ownerRepository.findAll();

        if (!ownerEntityList.isEmpty()) {
            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header: HEADERS) {

                csvContent.append(header).append(",");

                if (count == HEADERS.length) {
                    csvContent.append(header).append("\n");
                }
                count++;
            }

            for (OwnerEntity owner : ownerEntityList) {
                csvContent
                        .append(owner.getId()).append(",")
                        .append(owner.getDni()).append(",")
                        .append(owner.getEmail()).append(",")
                        .append(owner.getName()).append(",")
                        .append(owner.getLastName()).append(",")
                        .append(owner.getPhoneNumber()).append("\n");

            }

            return csvContent.toString();

        }

        log.error("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}", 0);
        throw new RuntimeException();
    }

    /**
     * Retrieves information about owners in JSON format.
     *
     * @return A JSON string representing information about owners.
     * @throws Exception Throws an exception if no owner entities are found in the database.
     */
    @Override
    public String ownerInfoDownloadJson() throws Exception {

        List<OwnerEntity> ownerEntityList = ownerRepository.findAll();

        if (!ownerEntityList.isEmpty()) {
            String ownerJson = objectMapper.writeValueAsString(ownerEntityList);

            return ownerJson;
        }

        throw new Exception("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}" + 0);
    }

    /**
     * Retrieves an owner by their email address.
     *
     * @param email The email address of the owner to retrieve.
     * @return An OwnerResponse object representing the owner found.
     * @throws Exception Throws an exception if the owner with the specified email address is not found.
     */
    @Override
    public OwnerResponse findByEmail(String email) throws Exception {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findByEmail(email);

        if (optionalOwnerEntity.isPresent()) {
            OwnerResponse ownerResponse = modelMapper.map(optionalOwnerEntity.get(), OwnerResponse.class);
            log.info("Owner found with email -> {}", email);
            return ownerResponse;
        }

        throw new Exception("Owner not found with email -> " + email);
    }

    /**
     * Retrieves an owner by their DNI (Documento Nacional de Identidad).
     *
     * @param dni The DNI of the owner to retrieve.
     * @return An OwnerResponse object representing the owner found.
     * @throws Exception Throws an exception if the owner with the specified DNI is not found.
     */
    @Override
    public OwnerResponse findByDni(String dni) throws Exception {
        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findByDni(dni);

        if (optionalOwnerEntity.isPresent()) {
            OwnerResponse ownerResponse = modelMapper.map(optionalOwnerEntity.get(), OwnerResponse.class);
            log.info("Owner found with DNI -> {}", dni);
            return ownerResponse;
        }

        throw new Exception("Owner not found with DNI -> " + dni);
    }

}
