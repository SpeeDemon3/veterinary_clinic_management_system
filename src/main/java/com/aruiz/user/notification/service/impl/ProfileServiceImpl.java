package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.ProfileRequest;
import com.aruiz.user.notification.controller.dto.ProfileResponse;
import com.aruiz.user.notification.controller.dto.UserRequestUpdate;
import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.domain.Profile;
import com.aruiz.user.notification.entity.ProfileEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.ProfileRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.ProfileService;
import com.aruiz.user.notification.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProfileResponse save(Long userId, ProfileRequest profileRequest) throws Exception {
        // Verifica si el usuario existe en la base de datos
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            // Lanza una excepción si el usuario no existe
            log.error("User not found with ID: " + userId);
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }

        // Obtén el usuario de la base de datos
        UserEntity userEntity = optionalUser.get();

        // Mapea la solicitud de perfil a una entidad de perfil
        ProfileEntity profileEntity = modelMapper.map(profileRequest, ProfileEntity.class);

        // Establece la relación entre el usuario y el perfil
        userEntity.setProfile(profileEntity);
        profileEntity.setUser(userEntity);

        // Guarda tanto el usuario como el perfil en la base de datos
        userRepository.save(userEntity);
        profileRepository.save(profileEntity);

        // Mapea el perfil guardado a una respuesta de perfil y devuélvela
        return modelMapper.map(profileEntity, ProfileResponse.class);
    }

    @Override
    public ProfileResponse findById(Long id) throws Exception {

        Optional<ProfileEntity> optionalProfileEntity = profileRepository.findById(id);

        if (optionalProfileEntity.isPresent()) {
            ProfileResponse profileResponse = modelMapper.map(optionalProfileEntity.get(), ProfileResponse.class);

            return profileResponse;

        } else {
            log.error("Id is not found!!!");
            throw new Exception();
        }

    }


    @Override
    public List<ProfileResponse> findAll() throws Exception {

        List<ProfileEntity> profileEntityList = profileRepository.findAll();

        if (profileEntityList.isEmpty()) {
            log.error("There are not profiles");
            throw new Exception();
        } else {
            List<ProfileResponse> profileResponseList = new ArrayList<>();

            profileEntityList.forEach(profile -> profileResponseList.add(modelMapper.map(profile, ProfileResponse.class)));

            return profileResponseList;
        }
    }

    @Override
    public String deleteByID(Long id) throws Exception {

        Optional<ProfileEntity> profileEntityOptional = profileRepository.findById(id);

        if (profileEntityOptional.isPresent()) {
            profileRepository.deleteById(id);
        }
        log.error("Id not found!!!");

        return "ERROR!!! Id is not found!!!";

    }

    @Override
    public ProfileResponse updateById(Long id, ProfileRequest profileRequest) throws Exception {

        Optional<ProfileEntity> optionalProfileEntity = profileRepository.findById(id);

        if (optionalProfileEntity.isPresent()) {


            if (profileRequest.getImg() == null) {
                profileRequest.setImg(optionalProfileEntity.get().getImg());
            }

            if (profileRequest.getBirthdate() == null) {
                profileRequest.setBirthdate(optionalProfileEntity.get().getBirthdate());
            }

            ProfileEntity profileEntity = modelMapper.map(profileRequest, ProfileEntity.class);

            profileEntity.setId(id);

            profileRepository.save(profileEntity);

            return modelMapper.map(profileEntity, ProfileResponse.class);

        }

        log.error("Id not found!!!!");

        throw new Exception();
    }
}
