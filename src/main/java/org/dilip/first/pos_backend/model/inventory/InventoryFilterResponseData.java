package org.dilip.first.pos_backend.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryFilterResponseData {

    private Long productId;
    private Long clientId;
    private String productName;
    private String barcode;
    private Double mrp;
    private Long quantity;
}
