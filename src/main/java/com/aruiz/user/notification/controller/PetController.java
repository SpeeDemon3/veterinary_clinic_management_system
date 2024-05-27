package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.service.impl.PetServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet")
@Slf4j
@RequiredArgsConstructor
public class PetController {

    private final PetServiceImpl petService;

    @PostMapping("/add/{ownerId}")
    public ResponseEntity<?> addPet(@PathVariable Long ownerId, @RequestBody PetRequest petRequest) {
        try {
            return ResponseEntity.ok(petService.save(ownerId, petRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
