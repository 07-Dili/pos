package org.dilip.first.pos_backend.controller;

import org.dilip.first.pos_backend.dto.InvoiceDto;
import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

        @Autowired
        private InvoiceDto invoiceDto;

        @PostMapping("/generate/{orderId}")
        public ResponseEntity<InvoiceEntity> generateInvoice(@PathVariable Long orderId) {
                InvoiceEntity invoice = invoiceDto.generateInvoice(orderId);
                return ResponseEntity.ok(invoice);
        }

        @GetMapping("/{orderId}/download")
        public ResponseEntity<FileSystemResource> download(@PathVariable Long orderId) {

                FileSystemResource file = invoiceDto.getInvoicePdf(orderId);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=" + file.getFilename())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(file);
        }
}

