package org.dilip.first.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDto {

    private Long orderId;
    private LocalDate orderDate;
    private Double totalAmount;
    private List<InvoiceItemDto> items;
}
