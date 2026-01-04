package org.dilip.first.pos_backend.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceData {

    private Long invoiceId;
    private Long orderId;
    private String filePath;
}
