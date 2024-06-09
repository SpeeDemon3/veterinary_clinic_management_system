package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.service.impl.OwnerServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerController {

    private final OwnerServiceImp ownerServiceImp;

    @PostMapping("/add/{petId}")
    public ResponseEntity<?> addOwner(@PathVariable Long petId, @RequestBody OwnerRequest ownerRequest) {
        try {
            return ResponseEntity.ok(ownerServiceImp.save(petId, ownerRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        try {
            log.info(email);
            return ResponseEntity.ok(ownerServiceImp.findByEmail(email));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByDni/{dni}")
    public ResponseEntity<?> findByDni(@PathVariable String dni) {
        try {
            log.info(dni);
            return ResponseEntity.ok(ownerServiceImp.findByDni(dni));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ownerServiceImp.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(ownerServiceImp.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ownerServiceImp.deleteById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody OwnerRequest ownerRequest) {
        try {
            return ResponseEntity.ok(ownerServiceImp.updateById(id, ownerRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
