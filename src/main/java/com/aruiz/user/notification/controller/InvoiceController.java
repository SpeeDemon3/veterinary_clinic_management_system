package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.service.impl.InvoiceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceServiceImpl invoiceService;

}
