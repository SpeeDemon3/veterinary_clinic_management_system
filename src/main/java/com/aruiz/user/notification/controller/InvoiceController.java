package com.aruiz.user.notification.controller;

import com.aruiz.user.notification.controller.dto.InvoiceRequest;
import com.aruiz.user.notification.service.impl.InvoiceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling invoice-related HTTP requests.
 *
 * @author Antonio Ruiz
 */
@RestController
@RequestMapping("/api/invoice")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {

    private final InvoiceServiceImpl invoiceService;

    /**
     * Handles POST requests to add a new invoice for the specified owner.
     *
     * @param dniOwner        The DNI (identification number) of the owner for whom the invoice is being added.
     * @param invoiceRequest  The request body containing the details of the invoice to be added.
     * @return                A ResponseEntity containing the result of the invoice save operation. If successful,
     *                        returns an OK response with the saved invoice data. If an error occurs,
     *                        returns an internal server error response.
     */
    @PostMapping("/add/{dniOwner}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> add(@PathVariable String dniOwner, @RequestBody InvoiceRequest invoiceRequest) {
        try {
            return ResponseEntity.ok(invoiceService.save(dniOwner, invoiceRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Handles GET requests to find an invoice by its ID.
     *
     * @param id  The ID of the invoice to be retrieved.
     * @return    A ResponseEntity containing the invoice data if found.
     *            If the invoice is not found, returns a 404 Not Found response.
     *            If an internal server error occurs, returns a 500 Internal Server Error response.
     */
    @GetMapping("findById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findById (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(invoiceService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handles GET requests to find invoices by the client's DNI (identification number).
     *
     * @param clientDni  The DNI of the client whose invoices are to be retrieved.
     * @return           A ResponseEntity containing the list of invoices associated with the specified client DNI.
     *                   If an internal server error occurs, logs the error and returns a 500 Internal Server Error response.
     */
    @GetMapping("/findByClientDNI/{clientDni}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findByClientDni (@PathVariable String clientDni) {
        try {
            return ResponseEntity.ok(invoiceService.findByClientDni(clientDni));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Handles GET requests to find invoices by their state.
     *
     * @param state  The state of the invoices to be retrieved.
     * @return       A ResponseEntity containing the list of invoices with the specified state.
     *               If an internal server error occurs, logs the error and returns a 404 Not Found response.
     */
    @GetMapping("/findByState/{state}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findByState (@PathVariable String state) {
        try {
            return ResponseEntity.ok(invoiceService.findByState(state));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles GET requests to retrieve all invoices.
     *
     * @return  A ResponseEntity containing the list of all invoices.
     *          If an internal server error occurs, returns a 404 Not Found response.
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> findAll () {
        try {
            return ResponseEntity.ok(invoiceService.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles PUT requests to update an existing invoice by its ID.
     *
     * @param id              The ID of the invoice to be updated.
     * @param invoiceRequest  The request body containing the updated invoice details.
     * @return                A ResponseEntity containing the updated invoice data.
     *                        If the request is invalid, returns a 400 Bad Request response.
     *                        If an internal server error occurs, returns a 500 Internal Server Error response.
     */
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateById (@PathVariable Long id, @RequestBody InvoiceRequest invoiceRequest) {
        try {
            return ResponseEntity.ok(invoiceService.updateById(id, invoiceRequest));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * Handles DELETE requests to delete an invoice by its ID.
     *
     * @param id  The ID of the invoice to be deleted.
     * @return    A ResponseEntity indicating the result of the delete operation.
     *            If successful, returns an OK response.
     *            If an error occurs, returns a 404 Not Found response.
     */
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(invoiceService.deleteById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles GET requests to download a CSV file containing invoices data.
     *
     * @return  A ResponseEntity containing the CSV file as a byte array with appropriate headers for file download.
     *          If an error occurs, returns a 500 Internal Server Error response with an error message.
     */
    @GetMapping("/downloadFileCsvInvoices")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> downloadFileCsvInvoices () {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "invoices-data.csv");

            byte[] csvBytes = invoiceService.invoicesInfoDownloadCsv().getBytes();

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error downloading invoices data CSV!!!!");
        }
    }

    /**
     * Handles GET requests to download a JSON file containing invoices data.
     *
     * @return  A ResponseEntity containing the JSON file with appropriate headers for file download.
     *          If an error occurs, returns a 500 Internal Server Error response with an error message.
     */
    @GetMapping("/downloadFileJsonInvoices")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> downloadFileJsonInvoices () {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "invoices-data.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(invoiceService.invoicesInfoDownloadJson());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error downloading invoices data JSON!!!!");
        }
    }


}
