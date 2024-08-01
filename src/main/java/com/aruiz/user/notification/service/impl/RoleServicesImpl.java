package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.controller.dto.RoleResponse;
import com.aruiz.user.notification.domain.Role;
import com.aruiz.user.notification.entity.RoleEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.RoleRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Roles
 *
 * @author Antonio Ruiz
 */
@Service
@Slf4j
public class RoleServicesImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Saves a new role.
     *
     * @param roleRequest The role details to be saved.
     * @return A RoleResponse object representing the saved role.
     * @throws Exception Throws an exception if there are issues during the saving process or if required fields are missing.
     */
    @Override
    public RoleResponse save(RoleRequest roleRequest) throws Exception {

        if (roleRequest.getName() != null) {

            RoleEntity roleEntity = modelMapper.map(roleRequest, RoleEntity.class);

            RoleEntity roleEntitySave = roleRepository.save(roleEntity);

            log.info("Role saved successfully!!!");

            return modelMapper.map(roleEntitySave, RoleResponse.class);

        } else {
            log.error("Something went wrong!!!!");
            throw new Exception();
        }

    }

    /**
     * Finds a role by its ID.
     *
     * @param id The ID of the role to find.
     * @return A RoleResponse object representing the found role.
     * @throws Exception Throws an exception if the role with the specified ID is not found or if there are issues during the retrieval process.
     */
    @Override
    public RoleResponse findById(Long id) throws Exception {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(id);

        if (roleEntityOptional.isPresent()) {

            RoleResponse roleResponse = modelMapper.map(roleEntityOptional.get(), RoleResponse.class);

            return roleResponse;
        } else {
            log.error("Role not found!!!!");
            throw new Exception();
        }

    }

    /**
     * Retrieves all roles.
     *
     * @return A list of RoleResponse objects representing all roles.
     * @throws Exception Throws an exception if no roles are found or if there are issues during the retrieval process.
     */
    @Override
    public List<RoleResponse> findAll() throws Exception {

        Optional<List<RoleEntity>> optionalRoleEntityList = Optional.of(roleRepository.findAll());

        if (optionalRoleEntityList.isPresent()) {

            List<RoleEntity> roleEntityList = optionalRoleEntityList.get();

            List<RoleResponse> roleResponseList = new ArrayList<>();

            roleEntityList.forEach(roleEntity -> roleResponseList.add(modelMapper.map(roleEntity, RoleResponse.class)));

            log.info("Successfully found roles!!!");

            return roleResponseList;

        } else {
            log.error("Roles not found!!!");
            throw new Exception();
        }


    }

    /**
     * Updates a role by its ID.
     *
     * @param id The ID of the role to update.
     * @param roleRequest The updated role details.
     * @return A RoleResponse object representing the updated role.
     * @throws Exception Throws an exception if the role with the specified ID does not exist or if there are issues during the update process.
     */
    @Override
    public RoleResponse updateById(Long id, RoleRequest roleRequest) throws Exception {
        log.info("role request values: {}", roleRequest);

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        log.info("Found Optional values: {}", optionalRoleEntity.get());

        if (optionalRoleEntity.isPresent()) {
            log.info("Updating role...");
            RoleEntity roleEntity = optionalRoleEntity.get();

            if (roleRequest.getName() == null) {
                roleEntity.setName(optionalRoleEntity.get().getName());
            } else {
                roleEntity.setName(roleRequest.getName());
            }

            if (roleRequest.getDescription() == null) {
                roleEntity.setDescription(optionalRoleEntity.get().getDescription());
            } else {
                roleEntity.setDescription(roleRequest.getDescription());
            }

            roleEntity.setId(id);

            log.info("Values entity update: {}", roleEntity);
            roleRepository.save(roleEntity);
            return modelMapper.map(roleEntity, RoleResponse.class);
        } else {
            log.error("Role id does not exist: {}", id);
            throw new Exception();
        }

    }

    /**
     * Deletes a role by its ID.
     *
     * @param id The ID of the role to delete.
     * @return true if the role was successfully deleted, false otherwise.
     * @throws Exception Throws an exception if the role with the specified ID does not exist or if there are issues during the deletion process.
     */
    @Override
    public Boolean deleteById(Long id) throws Exception {

        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(id);

        if (roleEntityOptional.isPresent()) {
            log.info("Role successfully deleted!!!");
            roleRepository.deleteById(id);
            return true;
        } else {
            log.error("Role id does not exist: {}", id);
            log.error("False!!!!");
            throw new Exception();
        }
    }

    /**
     * Finds all roles by their name.
     *
     * @param name The name of the roles to find.
     * @return A list of RoleResponse objects representing the found roles.
     * @throws Exception Throws an exception if roles with the specified name are not found or if there are issues during the retrieval process.
     */
    @Override
    public List<RoleResponse> findAllByName(String name) throws Exception {

        Optional<List<RoleEntity>> optionalRoleEntityList = roleRepository.findAllByName(name);

        if (optionalRoleEntityList.isPresent()) {

            List<RoleEntity> roleEntityList = optionalRoleEntityList.get();

            log.info("Found " + name + " role!!!");

            List<RoleResponse> roleResponseList = new ArrayList<>();

            roleEntityList.forEach(role -> roleResponseList.add(modelMapper.map(role, RoleResponse.class)));

            return roleResponseList;

        } else {
            log.error("Roles not found!!!!");
            throw new Exception();
        }

    }

    /**
     * Finds a role by its name.
     *
     * @param name The name of the role to find.
     * @return A Role object representing the found role, or null if not found.
     * @throws Exception Throws an exception if there are issues during the retrieval process.
     */
    @Override
    public Role findByName(String name) throws Exception {

        Optional<RoleEntity> roleEntityOptional = roleRepository.findByName(name);

        if (roleEntityOptional.isPresent()) {

            RoleEntity roleEntity = roleEntityOptional.get();

            return modelMapper.map(roleEntity, Role.class);
        }

        log.error("Rol name not found!!!!");

        return null;
    }

}
