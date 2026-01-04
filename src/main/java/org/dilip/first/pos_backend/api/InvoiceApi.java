package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.InvoiceDao;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.dto.InvoiceItemDto;
import org.dilip.first.pos_backend.dto.InvoiceRequestDto;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.util.InvoiceFileUtil;
import org.dilip.first.pos_backend.util.InvoiceServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        OrderEntity order = orderDao.findById(orderId).orElseThrow(() -> new ApiException(404,"Order not found with id: " + orderId));
        List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);
        InvoiceRequestDto request = buildInvoiceRequest(order, orderItems);
        String pdfPath = invoiceServiceUtil.generateAndSavePdf(request);
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setOrderId(orderId);
        invoice.setPdfPath(pdfPath);
        return invoiceDao.save(invoice);
    }

    public InvoiceEntity getByOrderId(Long orderId) {
        return invoiceDao.findByOrderId(orderId).orElseThrow(() -> new ApiException(404,"Invoice not found for order id: " + orderId));
    }

    public byte[] downloadInvoice(Long invoiceId) {
        InvoiceEntity invoice = getByOrderId(invoiceId);
        return InvoiceFileUtil.readInvoice(invoice.getPdfPath());
    }

    private InvoiceRequestDto buildInvoiceRequest(OrderEntity order, List<OrderItemEntity> orderItems) {

        List<InvoiceItemDto> items = orderItems.stream().map(this::convertToInvoiceItemDto).collect(Collectors.toList());
        InvoiceRequestDto request = new InvoiceRequestDto();
        request.setOrderId(order.getId());
        request.setOrderDate(LocalDate.now());
        request.setTotalAmount(order.getTotalAmount());
        request.setItems(items);
        return request;
    }

    private InvoiceItemDto convertToInvoiceItemDto(OrderItemEntity orderItem) {
        ProductEntity product = productDao.findById(orderItem.getProductId()).orElse(null);
        String productName = product != null ? product.getName() : "Unknown Product";
        InvoiceItemDto dto = new InvoiceItemDto();
        dto.setName(productName);
        dto.setBarcode(orderItem.getBarcode());
        dto.setQuantity(orderItem.getQuantity());
        dto.setSellingPrice(orderItem.getSellingPrice());
        return dto;
    }
}
