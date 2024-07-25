package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.service.impl.OwnerServiceImp;
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

/**
 * Controller class for handling owner-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerController {

    private final OwnerServiceImp ownerServiceImp;

    /**
     * Endpoint to add an owner for a specific pet.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param petId The ID of the pet to which the owner will be added
     * @param ownerRequest The request body containing owner details
     * @return ResponseEntity containing the saved owner if successful,
     *         or an internal server error status if an exception occurs
     */
    @PostMapping("/add/{petId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> addOwner(@PathVariable Long petId, @RequestBody OwnerRequest ownerRequest) {
        try {
            return ResponseEntity.ok(ownerServiceImp.save(petId, ownerRequest));
        } catch (Exception e) {
            log.error("Error add owner: {}", e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Endpoint to find an owner by their email address.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param email The email address of the owner to find
     * @return ResponseEntity containing the owner if found,
     *         HttpStatus.NOT_FOUND if the owner is not found,
     *         HttpStatus.BAD_REQUEST if the request is malformed,
     *         or an internal server error status if an exception occurs
     */
    @GetMapping("/findByEmail/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    /**
     * Endpoint to find an owner by their DNI (Documento Nacional de Identidad).
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param dni The DNI (Documento Nacional de Identidad) of the owner to find
     * @return ResponseEntity containing the owner if found,
     *         HttpStatus.NOT_FOUND if the owner is not found,
     *         HttpStatus.BAD_REQUEST if the request is malformed,
     *         or an internal server error status if an exception occurs
     */
    @GetMapping("/findByDni/{dni}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findByDni(@PathVariable String dni) {
        try {
            log.info(dni);
            return ResponseEntity.ok(ownerServiceImp.findByDni(dni));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error cause: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to find an owner by ID.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param id The ID of the owner to find
     * @return ResponseEntity containing the owner if found,
     *         HttpStatus.NOT_FOUND if the owner is not found,
     *         HttpStatus.BAD_REQUEST if the request is malformed,
     *         or an internal server error status if an exception occurs
     */
    @GetMapping(value = "/findById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    /**
     * Endpoint to retrieve all owners.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @return ResponseEntity containing a list of owners if found,
     *         HttpStatus.NO_CONTENT if no owners are found,
     *         or an internal server error status if an exception occurs
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(ownerServiceImp.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Error cause: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete an owner by ID.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param id The ID of the owner to delete
     * @return ResponseEntity indicating success if the owner is deleted,
     *         or an appropriate error status if an exception occurs
     */
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    /**
     * Endpoint to update owner information by ID.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @param id The ID of the owner to update
     * @param ownerRequest The updated owner information
     * @return ResponseEntity containing the updated owner information if successful,
     *         or an appropriate error status if an exception occurs
     */
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    /**
     * Endpoint to download owner information in CSV format.
     * This endpoint is accessible to users with either 'ROLE_USER' or 'ROLE_ADMIN' roles.
     *
     * @return ResponseEntity containing the CSV file with owner information as an attachment,
     *         or an appropriate error status if an exception occurs
     */
    @GetMapping("/downloadInfoFileCsv")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> downloadInfoFileCsv() {

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "owners-data.csv");

            byte[] csvBytes = ownerServiceImp.ownerInfoDownloadCsv().getBytes();

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * Endpoint to download owner information in JSON format.
     * This endpoint is accessible only to users with the 'ROLE_ADMIN' role.
     *
     * @return ResponseEntity containing the JSON file with owner information as an attachment,
     *         or an appropriate error status if an exception occurs
     */
    @GetMapping("/downloadInfoFileJson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> downloadInfoFileJson() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "owners.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ownerServiceImp.ownerInfoDownloadJson());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
