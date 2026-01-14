package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.InvoiceDao;
import org.dilip.first.pos_backend.flow.InvoiceFlow;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvoiceApi {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceFlow invoiceFlow;

    public InvoiceEntity getByOrderId(Long orderId) {
        InvoiceEntity invoice = invoiceDao.findByOrderId(orderId);
        if (invoice == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invoice not found for order id: " + orderId);
        }
        return invoice;
    }

    public InvoiceEntity generateInvoice(Long orderId) {
        return invoiceFlow.generateInvoice(orderId);
    }
}
