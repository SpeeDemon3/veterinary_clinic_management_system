package com.aruiz.user.notification.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
    private String invoiceNumber;
    private Double totalPrice;
    private String state;
}
