package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Owner client;
    private LocalDate dateOfIssue;
    private String state;
}
