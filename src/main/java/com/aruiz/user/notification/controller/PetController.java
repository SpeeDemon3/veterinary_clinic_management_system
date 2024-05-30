package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.service.impl.PetServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllPets() {
        try {
            return ResponseEntity.ok(petService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.deleteById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody PetRequestUpdate petRequestUpdate) {
        try {
            return ResponseEntity.ok(petService.updateById(id, petRequestUpdate));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/petImg/{id}/add")
    public ResponseEntity<String> addPetImg(@PathVariable Long id, @RequestParam("imageFile")MultipartFile imageFile) {
        try {

            if (!imageFile.getOriginalFilename().contains(".png")) {
                log.error("The image must be in PNG format!!!!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            petService.addPetImg(id, imageFile);

            log.info("Image name: {}", imageFile.getOriginalFilename());
            log.info("Image size: {}", imageFile.getSize());

            return ResponseEntity.ok("Image successfully saved!!!");
        } catch (Exception e) {
            log.error("The image could not be added to the user.");
            log.info(imageFile.getOriginalFilename());
            log.info(String.valueOf(imageFile.getSize()));
            log.info(imageFile.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/petImg/{id}")
    public ResponseEntity<byte[]> getPetImg(@PathVariable Long id) {
        try {

            byte[] imgBytes = petService.getPetImg(id);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);

            log.info("Recovering image....");

            return new ResponseEntity<>(imgBytes, httpHeaders, HttpStatus.OK);

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value= "/downloadFilePets")
    public ResponseEntity<?> downloadFileUsers() throws IOException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "pets-data.csv");

        byte[] csvBytes = petService.petsInfoDownloadCsv().getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

    }

}
