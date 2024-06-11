package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.repository.InvoiceRepository;
import com.aruiz.user.notification.service.InvoiceService;
import com.aruiz.user.notification.service.OwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    // private final String[] HEADERS =

    @Override
    public InvoiceResponse save(String clientDni, InvoiceRequest invoiceRequest) throws Exception {

        OwnerResponse ownerResponse = ownerService.findByDni(clientDni);

        if (invoiceRequest != null && ownerResponse != null) {

            InvoiceEntity invoiceEntitySave = modelMapper.map(invoiceRequest, InvoiceEntity.class);

            invoiceEntitySave.setDateOfIssue(LocalDate.now());

            invoiceEntitySave.setClient(modelMapper.map(ownerResponse, OwnerEntity.class));

            invoiceRepository.save(invoiceEntitySave);

            return modelMapper.map(invoiceEntitySave, InvoiceResponse.class);
        }

        throw new Exception("INTERNAL SERVER ERROR!!!!");
    }

    @Override
    public InvoiceResponse findById(Long id) throws Exception {
        return null;
    }

    @Override
    public InvoiceResponse findByClient(OwnerEntity client) throws Exception {
        return null;
    }

    @Override
    public List<InvoiceResponse> findByState(String state) throws Exception {
        return List.of();
    }

    @Override
    public List<InvoiceResponse> findAll() throws Exception {
        return List.of();
    }

    @Override
    public InvoiceResponse updateById(Long id, InvoiceRequest invoiceRequest) throws Exception {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) throws Exception {
        return null;
    }

    @Override
    public String invoicesInfoDownloadCsv() throws Exception {
        return "";
    }

    @Override
    public String invoicesInfoDownloadJson() throws Exception {
        return "";
    }
}
