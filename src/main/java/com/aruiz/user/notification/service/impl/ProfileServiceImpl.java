package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.ProfileRequest;
import com.aruiz.user.notification.controller.dto.ProfileResponse;
import com.aruiz.user.notification.domain.Profile;
import com.aruiz.user.notification.entity.ProfileEntity;
import com.aruiz.user.notification.repository.ProfileRepository;
import com.aruiz.user.notification.service.ProfileService;
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

    @Override
    public ProfileResponse save(ProfileRequest profileRequest) throws Exception {

        if (profileRequest != null) {

            ProfileEntity profileEntity = modelMapper.map(profileRequest, ProfileEntity.class);

            profileRepository.save(profileEntity);

            return modelMapper.map(profileEntity, ProfileResponse.class);
        } else {
            throw new Exception();
        }

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

            ProfileEntity profileEntity = optionalProfileEntity.get();
            profileEntity.setId(id);

            profileRepository.save(profileEntity);

            return modelMapper.map(profileEntity, ProfileResponse.class);

        }

        log.error("Id not found!!!!");

        throw new Exception();
    }
}
