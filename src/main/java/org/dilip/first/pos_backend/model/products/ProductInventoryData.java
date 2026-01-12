package org.dilip.first.pos_backend.model.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInventoryData {

    private Long productId;
    private Long clientId;
    private String productName;
    private String barcode;
    private Double mrp;
    private Long quantity;
}

