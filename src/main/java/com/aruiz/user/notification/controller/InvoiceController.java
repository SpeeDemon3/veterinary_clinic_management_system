package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.service.impl.InvoiceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
