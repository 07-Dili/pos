package org.dilip.first.pos_backend.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {

    private String barcode;
    private Long quantity;
    private Double sellingPrice;
}
