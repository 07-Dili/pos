package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.InvoiceApi;
import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.*;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.invoice.InvoiceItemForm;
import org.dilip.first.pos_backend.model.invoice.InvoiceRequestForm;
import org.dilip.first.pos_backend.util.helper.InvoiceServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Transactional
public class InvoiceFlow {

    @Autowired
    private InvoiceApi invoiceApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InvoiceServiceUtil invoiceServiceUtil;

    public InvoiceEntity generateInvoice(Long orderId) {

        InvoiceEntity existingInvoice = invoiceApi.getByOrderIdWithoutException(orderId);
        if (existingInvoice != null) {
            return existingInvoice;
        }

        OrderEntity order = orderApi.getById(orderId);
        if (order == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Order not found with id: " + orderId);
        }

        List<OrderItemEntity> orderItems = orderApi.findOrderItemsByOrderId(orderId);

        InvoiceRequestForm request = buildInvoiceRequest(order, orderItems);
        String pdfPath = invoiceServiceUtil.generateAndSavePdf(request);
        orderApi.changeOrderStatusToInvoice(order);

        return invoiceApi.generateInvoice(orderId, pdfPath);

    }

    private InvoiceRequestForm buildInvoiceRequest(OrderEntity order, List<OrderItemEntity> orderItems) {

        List<InvoiceItemForm> items = orderItems.stream().map(this::toInvoiceItem).collect(Collectors.toList());

        InvoiceRequestForm request = new InvoiceRequestForm();
        request.setOrderId(order.getId());
        request.setOrderDate(LocalDateTime.now().toString());
        request.setTotalAmount(order.getTotalAmount());
        request.setItems(items);

        return request;
    }

    private InvoiceItemForm toInvoiceItem(OrderItemEntity orderItem) {

        ProductEntity product = productApi.getById(orderItem.getProductId());
        String productName = product != null ? product.getName() : "Unknown Product";

        InvoiceItemForm form = new InvoiceItemForm();
        form.setName(productName);
        form.setBarcode(orderItem.getBarcode());
        form.setQuantity(orderItem.getQuantity());
        form.setSellingPrice(orderItem.getSellingPrice());

        return form;
    }
}




