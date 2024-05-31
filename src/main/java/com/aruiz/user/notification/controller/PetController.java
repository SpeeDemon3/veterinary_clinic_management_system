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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/pet")
@Slf4j
@RequiredArgsConstructor
public class PetController {

    private final PetServiceImpl petService;

    /**
     * Handles HTTP POST requests to add a new pet.
     *
     * @param ownerId The ID of the owner of the pet.
     * @param petRequest The request containing pet information.
     * @return ResponseEntity containing the details of the added pet.
     */
    @PostMapping("/add/{ownerId}")
    @PreAuthorize("hasRole('ROLE_VETERINARIAN')")
    public ResponseEntity<?> addPet(@PathVariable Long ownerId, @RequestBody PetRequest petRequest) {
        try {
            return ResponseEntity.ok(petService.save(ownerId, petRequest));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles HTTP GET requests to retrieve a pet by ID.
     *
     * @param id The ID of the pet to retrieve.
     * @return ResponseEntity containing the details of the retrieved pet.
     */
    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ROLE_VETERINARIAN')")
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

    /**
     * Handles HTTP GET requests to retrieve all pets.
     *
     * @return ResponseEntity containing the list of pets.
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_VETERINARIAN')")
    public ResponseEntity<?> findAllPets() {
        try {
            return ResponseEntity.ok(petService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles HTTP DELETE requests to delete a pet by ID.
     *
     * @param id The ID of the pet to delete.
     * @return ResponseEntity indicating the status of the deletion.
     */
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN'. 'ROLE_VETERINARIAN')")
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

    /**
     * Handles HTTP PUT requests to update a pet by ID.
     *
     * @param id The ID of the pet.
     * @param petRequestUpdate The updated information of the pet.
     * @return ResponseEntity containing the updated pet details.
     */
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN','ROLE_ADMIN')")
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

    /**
     * Handles HTTP POST requests to add an image to a pet.
     *
     * @param id The ID of the pet.
     * @param imageFile The image file to be added.
     * @return ResponseEntity indicating the status of the image addition.
     */
    @PostMapping("/petImg/{id}/add")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN','ROLE_USER')")
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

    /**
     * Handles HTTP GET requests to retrieve a pet's image.
     *
     * @param id The ID of the pet.
     * @return ResponseEntity containing the pet's image.
     */
    @GetMapping("/petImg/{id}")
    @PreAuthorize("hasRole('ROLE_VETERINARIAN')")
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

    /**
     * Handles HTTP GET requests to download a CSV file containing pet information.
     *
     * @return ResponseEntity containing the CSV file to download.
     * @throws IOException if an I/O error occurs while creating the CSV file.
     */
    @GetMapping(value= "/downloadFileCsvPets")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN','ROLE_ADMIN')")
    public ResponseEntity<?> downloadFileCsvPets() throws IOException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "pets-data.csv");

        byte[] csvBytes = petService.petsInfoDownloadCsv().getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

    }

    /**
     * Retrieves a pet by its identification code.
     *
     * @param code The identification code of the pet.
     * @return The response containing the details of the retrieved pet.
     */
    @GetMapping("/findByCode/{code}")
    @PreAuthorize("hasRole('ROLE_VETERINARIAN')")
    public ResponseEntity<?> findByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(petService.findByIdentificationCode(code));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles HTTP GET requests to download a JSON file containing information about pets.
     *
     * @return ResponseEntity containing the JSON file with appropriate headers for download.
     */
    @GetMapping("/downloadFileJsonPets")
    @PreAuthorize("hasAnyRole('ROLE_VETERINARIAN','ROLE_ADMIN')")
    public ResponseEntity<?> downloadFileJsonPets() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "pets.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(petService.petsInfoDownloadJson());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading pets JSON!!!!");
        }

    }

}
