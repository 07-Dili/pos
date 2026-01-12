package org.dilip.first.pos_backend.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemForm {

    private String name;
    private String barcode;
    private Long quantity;
    private Double sellingPrice;
}
