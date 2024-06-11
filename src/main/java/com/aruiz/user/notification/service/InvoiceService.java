package com.aruiz.user.notification.service;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.entity.OwnerEntity;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse save (String clientDni, InvoiceRequest invoiceRequest) throws Exception;
    InvoiceResponse findById(Long id) throws Exception;
    List<InvoiceResponse> findByClientDni(String clientDni) throws Exception;
    List<InvoiceResponse> findByState(String state) throws Exception;
    List<InvoiceResponse> findAll() throws Exception;
    InvoiceResponse updateById(Long id, InvoiceRequest invoiceRequest) throws Exception;
    Boolean deleteById(Long id) throws Exception;
    String invoicesInfoDownloadCsv() throws Exception;
    String invoicesInfoDownloadJson() throws Exception;
}
