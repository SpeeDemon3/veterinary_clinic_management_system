package com.aruiz.user.notification.controller;


import com.aruiz.user.notification.controller.dto.ProfileRequest;
import com.aruiz.user.notification.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> add(@PathVariable  Long id, @RequestBody ProfileRequest profileRequest) {
        try {
            return ResponseEntity.ok(profileService.save(profileRequest, id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
