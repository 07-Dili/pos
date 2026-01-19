package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InvoiceApi;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.dilip.first.pos_backend.flow.InvoiceFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDto {

    @Autowired
    private InvoiceApi invoiceApi;

    @Autowired
    private InvoiceFlow invoiceFlow;

    public InvoiceEntity generateInvoice(Long orderId) {
        return invoiceFlow.generateInvoice(orderId);
    }

    public FileSystemResource getInvoicePdf(Long orderId) {
        InvoiceEntity invoice = invoiceApi.getByOrderId(orderId);
        return new FileSystemResource(invoice.getPdfPath());
    }
}

