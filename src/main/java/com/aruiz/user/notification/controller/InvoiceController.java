package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.service.impl.InvoiceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceServiceImpl invoiceService;

    @PostMapping("/add/{dniOwner}")
    public ResponseEntity<?> add(@PathVariable String dniOwner, @RequestBody InvoiceRequest invoiceRequest) {
        try {
            return ResponseEntity.ok(invoiceService.save(dniOwner, invoiceRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<?> findById (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(invoiceService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findByClientDNI/{clientDni}")
    public ResponseEntity<?> findByClientDni (@PathVariable String clientDni) {
        try {
            return ResponseEntity.ok(invoiceService.findByClientDni(clientDni));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findByState/{state}")
    public ResponseEntity<?> findByState (@PathVariable String state) {
        try {
            return ResponseEntity.ok(invoiceService.findByState(state));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll () {
        try {
            return ResponseEntity.ok(invoiceService.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateById (@PathVariable Long id, @RequestBody InvoiceRequest invoiceRequest) {
        try {
            return ResponseEntity.ok(invoiceService.updateById(id, invoiceRequest));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(invoiceService.deleteById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
