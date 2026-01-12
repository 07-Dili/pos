package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.InvoiceDao;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.*;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.invoice.InvoiceItemForm;
import org.dilip.first.pos_backend.model.invoice.InvoiceRequestForm;
import org.dilip.first.pos_backend.util.helper.InvoiceServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Transactional
public class InvoiceFlow {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceServiceUtil invoiceServiceUtil;

    public InvoiceEntity generateInvoice(Long orderId) {

        InvoiceEntity existingInvoice = invoiceDao.findByOrderId(orderId);
        if (existingInvoice != null) {
            return existingInvoice;
        }

        OrderEntity order = orderDao.findById(OrderEntity.class,orderId);
        if (order == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Order not found with id: " + orderId);
        }

        List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);

        InvoiceRequestForm request = buildInvoiceRequest(order, orderItems);
        String pdfPath = invoiceServiceUtil.generateAndSavePdf(request);

        order.setStatus(OrderStatus.INVOICED);
        orderDao.save(order);

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setOrderId(orderId);
        invoice.setPdfPath(pdfPath);

        return invoiceDao.save(invoice);
    }

    private InvoiceRequestForm buildInvoiceRequest(OrderEntity order, List<OrderItemEntity> orderItems) {

        List<InvoiceItemForm> items = orderItems.stream().map(this::toInvoiceItem).collect(Collectors.toList());

        InvoiceRequestForm request = new InvoiceRequestForm();
        request.setOrderId(order.getId());
        request.setOrderDate(LocalDate.now().toString());
        request.setTotalAmount(order.getTotalAmount());
        request.setItems(items);

        return request;
    }

    private InvoiceItemForm toInvoiceItem(OrderItemEntity orderItem) {

        ProductEntity product = productDao.findById(ProductEntity.class,orderItem.getProductId());
        String productName = product != null ? product.getName() : "Unknown Product";

        InvoiceItemForm form = new InvoiceItemForm();
        form.setName(productName);
        form.setBarcode(orderItem.getBarcode());
        form.setQuantity(orderItem.getQuantity());
        form.setSellingPrice(orderItem.getSellingPrice());

        return form;
    }
}




