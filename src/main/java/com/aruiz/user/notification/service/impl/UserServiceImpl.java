package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Implement User Service
 *
 * @author Antonio Ruiz - speedemon
 * @version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String[] HEADERS = {"ID", "Birthdate", "DNI", "EMAIL", "Name", "Phone Number"};

    /**
     * Saves a new user based on the provided SignUpRequest.
     *
     * @param userRequest The SignUpRequest containing user information.
     * @return UserResponse containing the details of the saved user.
     * @throws Exception if there is an error during the save process.
     */
    @Override
    public UserResponse save(SignUpRequest userRequest) throws Exception {

        Optional<Optional<RoleEntity>> roleEntityOptional = Optional.ofNullable(roleRepository.findById(Long.valueOf(1)));

        if (roleEntityOptional.isPresent()) {

            RoleEntity roleEntity = modelMapper.map(roleEntityOptional.get(), RoleEntity.class);

            UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);

            if (userEntity.getRole() == null) {
                userEntity.setRole(roleEntity);
            }

            // Guarda la entidad de usuario en la base de datos
            userRepository.save(userEntity);
            // Registra información sobre la entidad guardada
            log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());
            // Mapea la entidad de usuario a una respuesta de usuario y la devuelve
            return modelMapper.map(userEntity, UserResponse.class);
        } else {
            log.info("Role not found!!!");
            throw new Exception("Default role with ID 1 not found!");
        }

    }

    /**
     * Saves a UserEntity to the database.
     *
     * @param userEntity The UserEntity to save.
     * @return The saved UserEntity.
     * @throws Exception if there is an error during the save process.
     */
    UserEntity saveEntity(UserEntity userEntity) throws Exception {

        Optional<Optional<RoleEntity>> roleEntityOptional = Optional.ofNullable(roleRepository.findById(Long.valueOf(1)));

        if (roleEntityOptional.isPresent()) {

            RoleEntity roleEntity = modelMapper.map(roleEntityOptional.get(), RoleEntity.class);

            // Mapea la solicitud a un objeto de actualización de usuario
            UserEntity userEntitySave = userEntity;
            // Establece el rol por defecto
            userEntitySave.setRole(roleEntity);
            // Guarda la entidad de usuario en la base de datos
            userRepository.save(userEntity);
            // Registra información sobre la entidad guardada
            log.info("Saving entity ID, name {}{}", userEntity.getId(), userEntity.getName());

            return userEntitySave;
        } else {
            log.info("Role not found!!!");
            return null;
        }

    }

    /**
     * Finds all users.
     *
     * @return List of UserResponse containing details of all users.
     * @throws Exception if there is an error during the search process.
     */
    @Override
    public List<UserResponse> findAll() throws Exception {
        // Obtiene la lista de entidades de usuario de la base de datos
        List<UserEntity> entityList = userRepository.findAll();
        // Verifica si la lista está vacía y lanza una excepción si es así
        if (entityList.isEmpty()) {
            log.error("There are no entities to recover!!!");
            throw new EntityNotFoundException("No users found in the database");
        }
        // Inicializa una lista para almacenar las respuestas de usuario
        List<UserResponse> userResponseList = new ArrayList<>();
        // Itera sobre las entidades de usuario y las mapea a respuestas de usuario
        for(UserEntity user : entityList) {
            log.info("Recovering users...");
            userResponseList.add(modelMapper.map(user, UserResponse.class));
            log.info("Info {}", user.toString());
        }
        // Devuelve la lista de respuestas de usuario
        return userResponseList;
    }

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user to find.
     * @return UserResponse containing the details of the found user.
     * @throws Exception if there is an error during the search process.
     */
    @Override
    public UserResponse findById(Long id) throws Exception {
        // Busca la entidad de usuario en la base de datos por su identificador
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        // Verifica si la entidad de usuario existe
        if (userEntityOptional.isEmpty()) {
            // Registra un mensaje de error si no se encuentra ningún usuario con el identificador dado y lanza una excepción
            log.error("User not found!!!");
            throw new EntityNotFoundException("User not found with ID -> " + id);
        } else {
            // Obtiene la entidad de usuario si se encuentra en la base de datos
            UserEntity userEntity = userEntityOptional.get();
            // Registra información sobre el usuario encontrado
            log.info("User found with ID -> {}", userEntity.getId());
            // Mapea la entidad de usuario a una respuesta de usuario y la devuelve
            return modelMapper.map(userEntity, UserResponse.class);
        }


    }

    @Override
    public UserResponse findByDni(String dni) throws Exception {

        Optional<UserEntity> optionalUserEntity = userRepository.findByDni(dni);

        if (optionalUserEntity.isPresent()) {
            UserResponse userResponse = modelMapper.map(optionalUserEntity.get(), UserResponse.class);
            log.info("User found with email -> {}", dni);
            return userResponse;
        }

        return null;
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return A message indicating the success of the deletion.
     * @throws Exception if there is an error during the deletion process.
     */
    @Override
    public String deleteById(Long id) throws Exception {
        // Verifica si existe un usuario con el identificador dado
        if(userRepository.existsById(id)) {
            // Elimina el usuario de la base de datos
            userRepository.deleteById(id);
            // Registra un mensaje de éxito en el log
            log.info("User successfully deleted");
            // Devuelve un mensaje indicando que el usuario ha sido eliminado exitosamente
            return "User with ID -> " + id + " successfully deleted!!!";
        } else {
            // Registra un mensaje de error si no se encuentra ningún usuario con el identificador dado y lanza una excepción
            log.error("User not found!!!");
            throw new Exception();
        }

    }

    /**
     * Updates a user by ID with the provided information.
     *
     * @param id The ID of the user to update.
     * @param userRequest  The request containing updated user information.
     * @return UserResponse containing the updated user details.
     * @throws Exception if there is an error during the update process.
     */
    @Override
    public UserResponse updateById(Long id, UserRequestUpdate userRequest) throws Exception {
        // Busca la entidad de usuario en la base de datos por su identificador
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        
        // Verifica si la entidad de usuario existe
        if (optionalUserEntity.isPresent()) {

            UserEntity userEntity = optionalUserEntity.get();

            if (userRequest.getName() == null) {
                userEntity.setName(optionalUserEntity.get().getName());
            } else {
                userEntity.setName(userRequest.getName());
            }

            if (userRequest.getEmail() == null) {
                userEntity.setEmail(optionalUserEntity.get().getEmail());
            } else {
                userEntity.setEmail(userRequest.getEmail());
            }

            if (userRequest.getPassword() == null) {
                userEntity.setPassword(optionalUserEntity.get().getPassword());
            } else {
                userEntity.setPassword(userRequest.getPassword());
            }

            if (userRequest.getDni() == null) {
                userEntity.setDni(optionalUserEntity.get().getDni());
            } else {
                userEntity.setDni(userRequest.getDni());
            }

            if (userRequest.getPhoneNumber() == null) {
                userEntity.setPhoneNumber(optionalUserEntity.get().getPhoneNumber());
            } else {
                userEntity.setPhoneNumber(userRequest.getPhoneNumber());
            }

            if (userRequest.getImg() == null) {
                userEntity.setImg(optionalUserEntity.get().getImg());
            } else {
                userEntity.setImg(userRequest.getImg());
            }

            if (userRequest.getBirthdate() == null) {
                userEntity.setBirthdate(optionalUserEntity.get().getBirthdate());
            } else {
                userEntity.setBirthdate(userRequest.getBirthdate());
            }

            // Establece el identificador de la entidad de usuario guardada con el identificador proporcionado
            userEntity.setId(id);
            // Guarda la entidad de usuario actualizada en la base de datos
            userRepository.save(userEntity);

            // Registra un mensaje de éxito en el log
            log.info("Entity successfully updated!!!");
            // Mapea la entidad de usuario guardada a una respuesta de usuario y la devuelve
            return modelMapper.map(userEntity, UserResponse.class);

        } else {
            // Registra un mensaje de error si no se encuentra ningún usuario con el identificador dado y lanza una excepción
            throw new EntityNotFoundException("User not found with ID -> " + id);
        }

    }

    /**
     * Loads user details by username (email).
     *
     * @param emailUser The email of the user to load.
     * @return UserDetails containing the details of the user.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String emailUser) {
        // Intenta buscar el usuario en la base de datos por su correo electrónico
        try {
            // Busca el usuario por su correo electrónico y devuelve los detalles del usuario si se encuentra
            return userRepository.findByEmail(emailUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailUser));
        } catch (Exception e) {
            // Lanza una excepción de RuntimeException si ocurre algún error durante la búsqueda del usuario
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds an image to the user identified by the given ID.
     *
     * @param id The ID of the user.
     * @param imageFile The image file to be added.
     * @throws IOException if there is an error reading the image file or if the user with the given ID is not found.
     */
    @Override
    public void addUserImg(Long id, MultipartFile imageFile) throws IOException {

        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isPresent()) {
            log.info("Saving pet image...");

            UserEntity userEntity = userEntityOptional.get();

            userEntity.setImg(Base64.getEncoder().encodeToString(imageFile.getBytes()));

            userRepository.save(userEntity);

        } else {
            throw new IOException();
        }

    }

    /**
     * Retrieves the image associated with the user identified by the given ID.
     *
     * @param id The ID of the user.
     * @return Byte array containing the user's image.
     * @throws Exception if the user with the given ID is not found or if there is an error decoding the image.
     */
    @Override
    public byte[] getUserImg(Long id) throws Exception {

        UserEntity userEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);

        return Base64.getDecoder().decode(userEntity.getImg());
    }

    /**
     * Generates CSV content containing information about all users in the database.
     *
     * @return String containing CSV content.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public String usersInfoDownloadCsv() throws IOException {

        List<UserEntity> userEntityList = userRepository.findAll();

        if (!userEntityList.isEmpty()) {
            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header : HEADERS) {

                csvContent.append(header).append(",");

                if (count == HEADERS.length) {
                    csvContent.append(header).append("\n");
                }
                count++;
            }

            for (UserEntity user : userEntityList) {
                csvContent
                        .append(user.getId()).append(",")
                        .append(user.getBirthdate()).append(",")
                        .append(user.getDni()).append(",")
                        .append(user.getEmail()).append(",")
                        .append(user.getName()).append(",")
                        .append(user.getPhoneNumber()).append("\n");
            }

            return csvContent.toString();
        }

        log.error("There aren't entities in database!!!!!!!!!!!!!! Numer entities= {}", 0);
        throw new RuntimeException();

    }

    /**
     * Retrieves information about users from the database and converts it to JSON format.
     *
     * @return JSON string containing information about users.
     * @throws Exception if there is an error during the process.
     */
    @Override
    public String usersInfoDownloadJson() throws Exception {

        List<UserEntity> userEntityList = userRepository.findAll();

        if (!userEntityList.isEmpty()) {
            // Convert the list of user entities to JSON format using ObjectMapper
            String usersJson = objectMapper.writeValueAsString(userEntityList);

            return usersJson;
        }

        throw new Exception();
    }

    /**
     * Updates the role of a user identified by their DNI.
     *
     * @param dni The DNI (Documento Nacional de Identidad) of the user.
     * @param idRole The ID of the role to be assigned to the user.
     * @return A message indicating the success of the role update operation.
     * @throws Exception If the user or role is not found, or if an unexpected error occurs.
     */
    public String updateRoleByDni (String dni, Long idRole) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findByDni(dni);

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(idRole);

        if (optionalUserEntity.isPresent() && optionalRoleEntity.isPresent()) {

            UserEntity userEntity = optionalUserEntity.get();
            RoleEntity roleEntity = optionalRoleEntity.get();

            userEntity.setRole(roleEntity);

            userRepository.save(userEntity);

            log.info("Role update user with DNI -> {}", dni);
            log.info("Role updated successfully!!!");


        }

        throw new Exception();

    }

}
