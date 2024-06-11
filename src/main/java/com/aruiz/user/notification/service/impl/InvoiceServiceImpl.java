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
import java.util.ArrayList;
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

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(id);

        if (optionalInvoiceEntity.isPresent()) {
            return modelMapper.map(optionalInvoiceEntity, InvoiceResponse.class);
        }

        throw new Exception("Invoice not found!!!");
    }

    @Override
    public List<InvoiceResponse> findByClientDni(String clientDni) throws Exception {

        Optional<OwnerResponse> optionalOwner = Optional.ofNullable(ownerService.findByDni(clientDni));

        if (optionalOwner.isPresent()) {

            Optional<List<InvoiceEntity>> optionalInvoiceEntity = Optional.of(invoiceRepository.findAll());

            if (optionalInvoiceEntity.isPresent()) {

                List<InvoiceResponse> invoiceResponseList = new ArrayList<>();

                for (InvoiceEntity invoice : optionalInvoiceEntity.get()) {

                    if (invoice.getClient().getDni().equals(optionalOwner.get().getDni())) {

                        invoiceResponseList.add(modelMapper.map(invoice, InvoiceResponse.class));

                        return invoiceResponseList;
                    }

                }

            }

        }

        throw new Exception("Client DNI not found!!!");
    }

    @Override
    public List<InvoiceResponse> findByState(String state) throws Exception {

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        log.info("Size invoiceEntityList -> {}",invoiceEntityList.size());

        if (!invoiceEntityList.isEmpty()) {

            List<InvoiceResponse> invoiceResponseList = new ArrayList<>();

            for (InvoiceEntity invoice : invoiceEntityList) {
                log.info("State is -> {}", invoice.getState());
                if (state.trim().equalsIgnoreCase(invoice.getState().trim().toString())) {

                    invoiceResponseList.add(modelMapper.map(invoice, InvoiceResponse.class));
                }
            }

        }
        throw new Exception("Not found STATES with value -> " + state);
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
