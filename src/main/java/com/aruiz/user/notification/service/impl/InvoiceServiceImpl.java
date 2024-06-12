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

    private final String[] HEADERS = {"ID", "DATE OF ISSUE", "INVOICE NUMBER", "STATE", "TOTAL PRICE", "CLIENT ID"};

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

            if (!invoiceResponseList.isEmpty()) {
                return invoiceResponseList;
            } else {
                throw new Exception("List response empty!!! Size -> " + invoiceResponseList.size());
            }
        }
        throw new Exception("Not found STATES with value -> " + state);
    }

    @Override
    public List<InvoiceResponse> findAll() throws Exception {

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        if (!invoiceEntityList.isEmpty()) {

            List<InvoiceResponse> invoiceResponseList = new ArrayList<>();

            for (InvoiceEntity invoice : invoiceEntityList) {

                invoiceResponseList.add(modelMapper.map(invoice, InvoiceResponse.class));

            }

            return invoiceResponseList;
        }

        throw new Exception("List is empty!!!! Size -> " + invoiceEntityList.size());
    }

    @Override
    public InvoiceResponse updateById(Long id, InvoiceRequest invoiceRequest) throws Exception {

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(id);

        if (optionalInvoiceEntity.isPresent()) {

            InvoiceEntity invoiceEntitySave = new InvoiceEntity();

            if (invoiceRequest.getInvoiceNumber() == null) {
                invoiceEntitySave.setInvoiceNumber(optionalInvoiceEntity.get().getInvoiceNumber());
            } else {
                invoiceEntitySave.setInvoiceNumber(invoiceRequest.getInvoiceNumber());
            }

            if (invoiceRequest.getTotalPrice() == null) {
                invoiceEntitySave.setTotalPrice(optionalInvoiceEntity.get().getTotalPrice());
            } else {
                invoiceEntitySave.setTotalPrice(invoiceRequest.getTotalPrice());
            }

            if (invoiceRequest.getState() == null) {
                invoiceEntitySave.setState(optionalInvoiceEntity.get().getState());
            } else {
                invoiceEntitySave.setState(invoiceRequest.getState());
            }

            invoiceEntitySave.setId(optionalInvoiceEntity.get().getId());
            invoiceEntitySave.setClient(optionalInvoiceEntity.get().getClient());
            invoiceEntitySave.setDateOfIssue(optionalInvoiceEntity.get().getDateOfIssue());

            invoiceRepository.save(invoiceEntitySave);

            return modelMapper.map(invoiceEntitySave, InvoiceResponse.class);

        }

        throw new Exception("Something has gone wrong!!!");
    }

    @Override
    public Boolean deleteById(Long id) throws Exception {

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(id);

        if (optionalInvoiceEntity.isPresent()) {

            invoiceRepository.deleteById(id);

            return  true;

        }

        throw new Exception("FALSE: Entity not found with ID -> " + id);
    }

    @Override
    public String invoicesInfoDownloadCsv() throws Exception {

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        if (!invoiceEntityList.isEmpty()) {
            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            for (String header : HEADERS) {
                csvContent.append(header).append(",");

                if (count == HEADERS.length) {
                    csvContent.append(header).append("\n");
                }
                count++;
            }

            for (InvoiceEntity invoice : invoiceEntityList) {
                csvContent
                        .append(invoice.getId()).append(",")
                        .append(invoice.getInvoiceNumber()).append(",")
                        .append(invoice.getTotalPrice()).append(",")
                        .append(invoice.getClient()).append(",")
                        .append(invoice.getDateOfIssue()).append(",")
                        .append(invoice.getState()).append("\n");
            }

            return csvContent.toString();

        }

        log.error("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}", 0);
        throw new RuntimeException();
    }

    @Override
    public String invoicesInfoDownloadJson() throws Exception {

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        if (!invoiceEntityList.isEmpty()) {
            String invoicesJson = objectMapper.writeValueAsString(invoiceEntityList);

            return invoicesJson;
        }

        throw new Exception("There aren't entities in database!!!!!!!!!!!!!! Number entities= " + 0);
    }
}
