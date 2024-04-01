package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.RoleRequest;
import com.aruiz.user.notification.controller.dto.RoleResponse;
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

@Service
@Slf4j
public class RoleServicesImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleResponse save(RoleRequest roleRequest) throws Exception {

        if (roleRequest.getName() != null) {

            Optional<UserEntity> optionalUserEntity = userRepository.findById(roleRequest.getUser());

            if (optionalUserEntity.isPresent()) {

                UserEntity userEntity = optionalUserEntity.get();

                RoleEntity roleEntity = modelMapper.map(roleRequest, RoleEntity.class);

                roleEntity.setUser(userEntity);

                if (userEntity.getRole() != null) {
                    roleEntity.setId(userEntity.getId());
                }

                log.info("User found");

                RoleEntity roleEntitySave = roleRepository.save(roleEntity);

                userEntity.setRole(roleEntitySave);

                userRepository.save(userEntity);

                log.info("Role saved successfully!!!");

                return modelMapper.map(roleEntitySave, RoleResponse.class);


            } else {
                log.error("User not found!!!!");
                throw new RuntimeException("User not found!!!");
            }

        } else {
            log.error("Something went wrong!!!!");
            throw new Exception();
        }

    }

    @Override
    public RoleResponse findById(Long id) throws Exception {
        boolean exists = roleRepository.existsById(id);

        if (exists) {
            return modelMapper.map(roleRepository.findById(id), RoleResponse.class);
        } else {
            log.error("Role not found!!!!");
            throw new Exception();
        }

    }

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

    @Override
    public RoleResponse updateById(Long id, RoleRequest roleRequest) throws Exception {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);

        if (optionalRoleEntity.isPresent()) {
            log.info("Updating role...");
            RoleEntity roleEntity = optionalRoleEntity.get();
            roleEntity.setId(id);
            return modelMapper.map(roleEntity, RoleResponse.class);
        } else {
            log.error("Role id does not exist: {}", id);
            throw new Exception();
        }

    }

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
}
