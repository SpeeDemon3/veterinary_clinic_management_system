package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.AppointmentRequest;
import com.aruiz.user.notification.controller.dto.AppointmentRequestUpdate;
import com.aruiz.user.notification.service.AppointmentService;
import com.aruiz.user.notification.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling appointment-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/appointment")
@Slf4j
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint to save a new appointment.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param idVeterinarian the ID of the veterinarian for the appointment
     * @param idPet the ID of the pet for the appointment
     * @param appointmentRequest the details of the appointment to be created
     * @return ResponseEntity containing the saved appointment, or an appropriate error status if an exception occurs
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/addAppointment/{idVeterinarian}/{idPet}")
    public ResponseEntity<?> save (@PathVariable Long idVeterinarian, @PathVariable  Long idPet, @RequestBody AppointmentRequest appointmentRequest) {
        try {
            return ResponseEntity.ok(appointmentService.save(idVeterinarian, idPet, appointmentRequest));
        } catch (BadRequestException e) {
            log.error("Error cause: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error cause: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to retrieve all appointments.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @return ResponseEntity containing the list of all appointments, or an appropriate error status if an exception occurs.
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAll (){
        try {
            return ResponseEntity.ok(appointmentService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to find an appointment by its ID.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param idAppointment the ID of the appointment to be found.
     * @return ResponseEntity containing the appointment details if found, or an appropriate error status if an exception occurs.
     */
    @GetMapping("/findById/{idAppointment}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findById (@PathVariable Long idAppointment) {
        try {
            return ResponseEntity.ok(appointmentService.findById(idAppointment));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to find appointments by the pet's ID.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param id the ID of the pet whose appointments are to be found.
     * @return ResponseEntity containing the list of appointments for the specified pet, or an error status if an exception occurs.
     */
    @GetMapping("/findAppointmentsByPetId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAppointmentsByPetId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByPetId(id));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to find appointments by the veterinarian's ID.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param id the ID of the veterinarian whose appointments are to be found.
     * @return ResponseEntity containing the list of appointments for the specified veterinarian, or an error status if an exception occurs.
     */
    @GetMapping("/findAppointmentsByVeterinarianId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAppointmentsByVeterinarianId (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByVeterinarianId(id));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to find appointments by their date.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param dateOfAppointment the date of the appointments to find.
     * @return ResponseEntity containing the list of appointments on the specified date, or an error status if an exception occurs.
     */
    @GetMapping("/findAppointmentsByDate/{dateOfAppointment}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAppointmentsByDate (@PathVariable String dateOfAppointment) {
        log.info("Date enter controller {}", dateOfAppointment);
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByDateOfAppointment(dateOfAppointment));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to delete an appointment by its ID.
     * This endpoint is accessible only to users with the 'ROLE_ADMIN' role.
     *
     * @param id the ID of the appointment to delete.
     * @return ResponseEntity indicating the result of the deletion operation.
     */
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById (@PathVariable Long id) {
        try  {
            return ResponseEntity.ok(appointmentService.deleteById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to update an appointment by its ID.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @param id the ID of the appointment to update.
     * @param requestUpdate the details to update the appointment with.
     * @return ResponseEntity containing the updated appointment information if successful, or an appropriate error response.
     */
        @PutMapping("/updateById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateById (@PathVariable Long id, @RequestBody AppointmentRequestUpdate requestUpdate) {
        try {
            return ResponseEntity.ok(appointmentService.updateById(id, requestUpdate));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error cause: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to download appointment information in CSV format.
     * This endpoint is accessible to users with either the 'ROLE_USER' or 'ROLE_ADMIN' role.
     *
     * @return ResponseEntity containing the CSV data of appointments with appropriate headers for download.
     */
    @GetMapping("/appointmentInfoDownloadCsv")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> appointmentInfoDownloadCsv () {

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "appointments.csv");

            byte[] csvBytes = appointmentService.appointmentInfoDownloadCsv().getBytes();

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * Endpoint to download appointment information in JSON format.
     * This endpoint is accessible only to users with the 'ROLE_ADMIN' role.
     *
     * @return ResponseEntity containing the JSON data of appointments with appropriate headers for download.
     */
    @GetMapping("/appointmentInfoDownloadJson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> appointmentInfoDownloadJson () {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "appointments.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(appointmentService.appointmentInfoDownloadJson());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
