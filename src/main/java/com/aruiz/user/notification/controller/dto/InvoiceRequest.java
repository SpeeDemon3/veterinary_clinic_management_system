package com.aruiz.user.notification.controller.dto;

import com.aruiz.user.notification.domain.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
    private Long id;
    private String invoiceNumber;
    private Owner client;
    private String state;
}
