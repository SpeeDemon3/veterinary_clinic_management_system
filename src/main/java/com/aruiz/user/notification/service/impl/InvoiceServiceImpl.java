package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.repository.InvoiceRepository;
import com.aruiz.user.notification.service.InvoiceService;
import com.aruiz.user.notification.service.OwnerService;
import com.aruiz.user.notification.service.converter.InvoiceConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link InvoiceService} interface.
 * Provides methods to manage invoices.
 *
 * @author Antonio Ruiz
 */
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

    @Autowired
    private InvoiceConverter invoiceConverter;

    /**
     * Array containing headers for invoice data, including ID, date of issue, invoice number, state, total price, and client ID.
     */
    private final String[] HEADERS = {"ID", "DATE OF ISSUE", "INVOICE NUMBER", "STATE", "TOTAL PRICE", "CLIENT ID"};

    /**
     * Saves a new invoice based on the provided client DNI and invoice request.
     *
     * @param clientDni       The DNI (identification number) of the client for whom the invoice is being saved.
     * @param invoiceRequest  The request object containing details of the invoice to be saved.
     * @return                An InvoiceResponse object representing the saved invoice.
     * @throws Exception      Throws an exception if either the invoiceRequest is null or no owner is found for the provided DNI.
     *                        This indicates an internal server error.
     */
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

    /**
     * Finds an invoice by its ID.
     *
     * @param id  The ID of the invoice to find.
     * @return    An InvoiceResponse object representing the found invoice.
     * @throws Exception  Throws an exception if no invoice is found with the specified ID.
     */
    @Override
    public InvoiceResponse findById(Long id) throws Exception {

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(id);

        if (optionalInvoiceEntity.isPresent()) {
            return modelMapper.map(optionalInvoiceEntity, InvoiceResponse.class);
        }

        throw new Exception("Invoice not found!!!");
    }

    /**
     * Finds invoices associated with a client identified by their DNI (identification number).
     *
     * @param clientDni  The DNI of the client whose invoices are to be retrieved.
     * @return           A list of InvoiceResponse objects representing invoices associated with the client.
     * @throws Exception Throws an exception if no client is found with the specified DNI or if there are no invoices found for the client.
     */
    @Override
    public List<InvoiceResponse> findByClientDni(String clientDni) throws Exception {

        log.info("Service: Finding invoices for client DNI: {}", clientDni);
        OwnerResponse ownerResponse = ownerService.findByDni(clientDni);
        log.info("Owner response: {}", ownerResponse);

        if (ownerResponse != null) {
            log.info("Owner DNI FOUND -> {}", ownerResponse.getDni());

            Optional<List<InvoiceEntity>> optionalInvoiceEntities = invoiceRepository.findByClientDni(clientDni);
            log.info("Invoices found: {}", optionalInvoiceEntities.isPresent() ? optionalInvoiceEntities.get().size() : 0);

            if (optionalInvoiceEntities.isPresent()) {

                List<InvoiceEntity> invoiceEntityList = optionalInvoiceEntities.get();

                if (!invoiceEntityList.isEmpty()) {

                    List<InvoiceResponse> invoiceResponseList = new ArrayList<>();

                    for (InvoiceEntity invoice : invoiceEntityList) {

                            log.info("Invoice value -> {}", invoice.getClient());

                            invoiceResponseList.add(invoiceConverter.toInvoiceResponse(invoice));

                        }

                    return invoiceResponseList;

                }

            }

        }

        throw new Exception("Client DNI not found!!!");
    }


    /*
    @Override
    public List<InvoiceResponse> findByClientDni(String clientDni) throws Exception {
        // Encuentra al propietario por DNI
        OwnerResponse owner = ownerService.findByDni(clientDni);
        log.info("DNI Owner {} ", owner.getDni());
        if (owner == null) {
            throw new Exception("Client DNI not found!!!");
        }

        // Encuentra las facturas del propietario
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByClientDni(clientDni);
        if (invoiceEntities == null || invoiceEntities.isEmpty()) {
            throw new Exception("No invoices found for the given client DNI!!!");
        }

        // Convierte las entidades de facturas a respuestas de facturas
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        for (InvoiceEntity invoice : invoiceEntities) {
            InvoiceResponse response = invoiceConverter.toInvoiceResponse(invoice);
            invoiceResponseList.add(response);
            log.info(response.toString());
        }

        return invoiceResponseList;
    }

     */

    /**
     * Finds invoices based on their state.
     *
     * @param state  The state of the invoices to be retrieved.
     * @return       A list of InvoiceResponse objects representing invoices with the specified state.
     * @throws Exception Throws an exception if no invoices are found with the specified state or if the list of invoice responses is empty.
     */
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

    /**
     * Retrieves all invoices.
     *
     * @return A list of InvoiceResponse objects representing all invoices.
     * @throws Exception Throws an exception if the list of invoices is empty.
     */
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

    /**
     * Updates an invoice identified by its ID with the details provided in the invoice request.
     *
     * @param id              The ID of the invoice to be updated.
     * @param invoiceRequest  The request object containing updated details of the invoice.
     * @return                An InvoiceResponse object representing the updated invoice.
     * @throws Exception      Throws an exception if no invoice is found with the specified ID or if there are issues during the update process.
     */
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

    /**
     * Deletes an invoice by its ID.
     *
     * @param id  The ID of the invoice to be deleted.
     * @return    A boolean indicating whether the deletion was successful.
     * @throws Exception  Throws an exception if no invoice is found with the specified ID.
     */
    @Override
    public Boolean deleteById(Long id) throws Exception {

        Optional<InvoiceEntity> optionalInvoiceEntity = invoiceRepository.findById(id);

        if (optionalInvoiceEntity.isPresent()) {

            invoiceRepository.deleteById(id);

            return  true;

        }

        throw new Exception("FALSE: Entity not found with ID -> " + id);
    }

    /**
     * Generates a CSV string containing information about all invoices.
     *
     * @return A String representing CSV content with invoice information.
     * @throws Exception Throws an exception if there are no invoices found or if there are issues during CSV generation.
     */
    @Override
    public String invoicesInfoDownloadCsv() throws Exception {

        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();

        if (!invoiceEntityList.isEmpty()) {
            StringBuilder csvContent = new StringBuilder();

            int count = 1;

            // Append headers
            for (String header : HEADERS) {
                csvContent.append(header).append(",");

                if (count == HEADERS.length) {
                    csvContent.append(header).append("\n");
                }
                count++;
            }

            // Append invoice details
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
        // Log error and throw exception if no invoices found
        log.error("There aren't entities in database!!!!!!!!!!!!!! Number entities= {}", 0);
        throw new RuntimeException();
    }

    /**
     * Generates a JSON string containing information about all invoices.
     *
     * @return A String representing JSON content with invoice information.
     * @throws Exception Throws an exception if there are no invoices found or if there are issues during JSON generation.
     */
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
