package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InvoiceApi;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.dilip.first.pos_backend.model.data.InvoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDto {

    @Autowired
    private InvoiceApi invoiceApi;

    public InvoiceData getByOrderId(Long orderId) {

        InvoiceEntity entity = invoiceApi.getByOrderId(orderId);

        InvoiceData data = new InvoiceData();
        data.setInvoiceId(entity.getId());
        data.setOrderId(entity.getOrderId());
        data.setFilePath(entity.getPdfPath());

        return data;
    }

    public byte[] download(Long orderId) {
        return invoiceApi.downloadInvoice(orderId);
    }
}
