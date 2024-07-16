package com.aruiz.user.notification.service.converter;

import com.aruiz.user.notification.controller.dto.InvoiceResponse;
import com.aruiz.user.notification.controller.dto.OwnerRequest;
import com.aruiz.user.notification.controller.dto.OwnerResponse;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.domain.Invoice;
import com.aruiz.user.notification.entity.InvoiceEntity;
import com.aruiz.user.notification.entity.OwnerEntity;
import com.aruiz.user.notification.entity.PetEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OwnerConverter {

    @Autowired
    private ModelMapper modelMapper;

    public OwnerEntity  toOwnerEntity(OwnerRequest ownerRequest) {

        OwnerEntity ownerEntity = new OwnerEntity();

        ownerEntity.setId(null);
        ownerEntity.setName(ownerRequest.getName());
        ownerEntity.setLastName(ownerRequest.getLastName());
        ownerEntity.setEmail(ownerRequest.getEmail());
        ownerEntity.setDni(ownerRequest.getDni());
        ownerEntity.setPhoneNumber(ownerRequest.getPhoneNumber());
        ownerEntity.setInvoices(null);
        ownerEntity.setPets(null);

        log.info("Conversion carried out successfully!!!");

        return ownerEntity;

    }

    public OwnerResponse toOwnerResponse(OwnerEntity ownerEntity) {
        OwnerResponse ownerResponse = new OwnerResponse();

        ownerResponse.setId(ownerEntity.getId());
        ownerResponse.setName(ownerEntity.getName());
        ownerResponse.setLastName(ownerEntity.getLastName());
        ownerResponse.setEmail(ownerEntity.getEmail());
        ownerResponse.setDni(ownerEntity.getDni());
        ownerResponse.setPhoneNumber(ownerEntity.getPhoneNumber());

        List<Invoice> invoiceEntityList = modelMapper.map(ownerEntity.getInvoices(), new TypeToken<List<Invoice>>() {}.getType());
        ownerResponse.setInvoices(invoiceEntityList);

        List<PetResponse> petEntityList = modelMapper.map(ownerEntity.getPets(), new TypeToken<List<PetResponse>>() {}.getType());
        ownerResponse.setPets(petEntityList);

        log.info("Conversion carried out successfully!!!");

        return ownerResponse;

    }

}
