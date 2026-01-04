package org.dilip.first.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {

    private String name;
    private String barcode;
    private Long quantity;
    private Double sellingPrice;
}
