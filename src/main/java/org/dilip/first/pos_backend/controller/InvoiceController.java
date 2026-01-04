package org.dilip.first.pos_backend.controller;

import org.dilip.first.pos_backend.api.InvoiceApi;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

        @Autowired
        private InvoiceApi invoiceApi;

        @PostMapping("/generate/{orderId}")
        public ResponseEntity<InvoiceEntity> generateInvoice(@PathVariable Long orderId) {
                InvoiceEntity invoice = invoiceApi.generateInvoice(orderId);
                return ResponseEntity.ok(invoice);
        }

        @GetMapping("/{orderId}/download")
        public ResponseEntity<FileSystemResource> download(@PathVariable Long orderId) {
                InvoiceEntity invoice = invoiceApi.getByOrderId(orderId);
                FileSystemResource file = new FileSystemResource(invoice.getPdfPath());
                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                                .contentType(MediaType.APPLICATION_PDF).body(file);
        }
}
