package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
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

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllPets() {
        try {
            return ResponseEntity.ok(petService.findAll());
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.deleteById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody PetRequestUpdate petRequestUpdate) {
        try {
            return ResponseEntity.ok(petService.updateById(id, petRequestUpdate));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
