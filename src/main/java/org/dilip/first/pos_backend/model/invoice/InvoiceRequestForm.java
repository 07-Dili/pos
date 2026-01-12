package org.dilip.first.pos_backend.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestForm {

    private Long orderId;
    private String orderDate;
    private Double totalAmount;
    private List<InvoiceItemForm> items;
}
