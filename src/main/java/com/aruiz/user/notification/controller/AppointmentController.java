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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
@Slf4j
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/addAppointment/{idVeterinarian}/{idPet}")
    public ResponseEntity<?> save (@PathVariable Long idVeterinarian, @PathVariable  Long idPet, @RequestBody AppointmentRequest appointmentRequest) {
        try {
            return ResponseEntity.ok(appointmentService.save(idVeterinarian, idPet, appointmentRequest));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll (){
        try {
            return ResponseEntity.ok(appointmentService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findById/{idApointment}")
    public ResponseEntity<?> findById (@PathVariable Long idApointment) {
        try {
            return ResponseEntity.ok(appointmentService.findById(idApointment));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAppointmentsByPetId/{id}")
    public ResponseEntity<?> findAppointmentsByPetId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByPetId(id));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAppointmentsByVeterinarianId/{id}")
    public ResponseEntity<?> findAppointmentsByVeterinarianId (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByVeterinarianId(id));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAppointmentsByDate/{dateOfAppointment}")
    public ResponseEntity<?> findAppointmentsByDate (@PathVariable String dateOfAppointment) {
        try {
            return ResponseEntity.ok(appointmentService.findAppointmentsByDateOfAppointment(dateOfAppointment));
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteById/{id}")
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

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById (@PathVariable Long id, @RequestBody AppointmentRequestUpdate requestUpdate) {
        try {
            return ResponseEntity.ok(appointmentService.updateById(id, requestUpdate));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException ntf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/appointmentInfoDownloadCsv")
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

    @GetMapping("/appointmentInfoDownloadJson")
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
