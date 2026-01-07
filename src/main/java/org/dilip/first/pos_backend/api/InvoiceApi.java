package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.InvoiceDao;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.model.form.InvoiceItemForm;
import org.dilip.first.pos_backend.model.form.InvoiceRequestForm;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.util.helper.InvoiceServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceApi {

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

        InvoiceEntity existingInvoice = invoiceDao.findByOrderId(orderId).orElse(null);
        if (existingInvoice != null) {
            return existingInvoice;
        }
        OrderEntity order = orderDao.findById(orderId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Order not found with id: " + orderId));
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

    public InvoiceEntity getByOrderId(Long orderId) {
        return invoiceDao.findByOrderId(orderId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Invoice not found for order id: " + orderId));
    }

    private InvoiceRequestForm buildInvoiceRequest(OrderEntity order, List<OrderItemEntity> orderItems) {

        List<InvoiceItemForm> items = orderItems.stream().map(this::convertToInvoiceItemForm).collect(Collectors.toList());
        InvoiceRequestForm request = new InvoiceRequestForm();
        request.setOrderId(order.getId());
        request.setOrderDate(LocalDate.now().toString());
        request.setTotalAmount(order.getTotalAmount());
        request.setItems(items);
        return request;
    }

    private InvoiceItemForm convertToInvoiceItemForm(OrderItemEntity orderItem) {
        ProductEntity product = productDao.findById(orderItem.getProductId()).orElse(null);
        String productName = product != null ? product.getName() : "Unknown Product";
        InvoiceItemForm form = new InvoiceItemForm();
        form.setName(productName);
        form.setBarcode(orderItem.getBarcode());
        form.setQuantity(orderItem.getQuantity());
        form.setSellingPrice(orderItem.getSellingPrice());
        return form;
    }
}
