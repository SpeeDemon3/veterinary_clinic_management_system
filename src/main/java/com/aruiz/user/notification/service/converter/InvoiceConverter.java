package com.aruiz.user.notification.service.converter;

import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.domain.Owner;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.service.OwnerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConverter {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ModelMapper modelMapper;

    public InvoiceResponse toInvoiceResponse (InvoiceEntity invoiceEntity) throws Exception {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        OwnerResponse ownerResponse = ownerService.findByDni(invoiceEntity.getClient().getDni());

        invoiceResponse.setId(invoiceEntity.getId());
        invoiceResponse.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        invoiceResponse.setTotalPrice(invoiceEntity.getTotalPrice());
        invoiceResponse.setClient(modelMapper.map(ownerResponse, Owner.class));
        invoiceResponse.setDateOfIssue(invoiceEntity.getDateOfIssue());
        invoiceResponse.setState(invoiceEntity.getState());

        return invoiceResponse;

    }

}
