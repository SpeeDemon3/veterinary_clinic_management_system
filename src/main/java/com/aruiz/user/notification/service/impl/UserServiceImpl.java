package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.*;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de usuario.
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


    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @autor: Antonio Ruiz
     * @version: 27/05/2024 V.1
     *
     * @param userRequest La solicitud de registro del usuario.
     * @return La respuesta del usuario guardado.
     * @throws Exception Si ocurre un error durante el proceso de guardado.
     */
    @Override
    public UserResponse save(SignUpRequest userRequest) throws Exception {

        Optional<Optional<RoleEntity>> roleEntityOptional = Optional.ofNullable(roleRepository.findById(Long.valueOf(1)));

        if (roleEntityOptional.isPresent()) {

            RoleEntity roleEntity = modelMapper.map(roleEntityOptional.get(), RoleEntity.class);
            /*
            // Mapea la solicitud a un objeto de actualización de usuario
            UserRequestUpdate userRequestUpdate = modelMapper.map(userRequest, UserRequestUpdate.class);
            // Establece el rol por defecto (1L)
            userRequestUpdate.setRole(roleEntity.getId());
            // Mapea la solicitud a una entidad de usuario
            */
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
     * Saves a given UserEntity with a default role.
     *
     * <p>This method fetches the default role with ID 1, sets it to the provided UserEntity,
     * and saves the entity in the database. If the role is not found, it logs a message and returns null.</p>
     *
     * @param userEntity the UserEntity to be saved
     * @return the saved UserEntity with the default role set, or null if the role is not found
     * @throws Exception if there is an issue during the role fetching or user saving process
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
     * Obtiene todos los usuarios almacenados en la base de datos.
     *
     * @return Una lista de respuestas de usuario.
     * @throws Exception Si no se encuentran usuarios en la base de datos.
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
     * Busca un usuario por su identificador único.
     *
     * @param id El identificador único del usuario a buscar.
     * @return La respuesta del usuario encontrado.
     * @throws Exception Si no se encuentra ningún usuario con el identificador proporcionado.
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

    /**
     * Elimina un usuario por su identificador único.
     *
     * @param id El identificador único del usuario a eliminar.
     * @return Un mensaje indicando que el usuario ha sido eliminado exitosamente.
     * @throws Exception Si no se encuentra ningún usuario con el identificador proporcionado.
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
     * Actualiza un usuario por su identificador único.
     *
     * @param id El identificador único del usuario a actualizar.
     * @param userRequest La solicitud de actualización del usuario.
     * @return La respuesta del usuario actualizado.
     * @throws Exception Si no se encuentra ningún usuario con el identificador proporcionado.
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
     * Carga un usuario por su nombre de usuario (en este caso, el correo electrónico).
     *
     * @param emailUser El correo electrónico del usuario.
     * @return Los detalles del usuario cargado.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el correo electrónico proporcionado.
     * @throws RuntimeException          Si ocurre algún error durante la búsqueda del usuario en la base de datos.
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

}
