package org.dilip.first.pos_backend.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterResponseData {

    private Long productId;
    private Long clientId;
    private String productName;
    private String barcode;
    private Double mrp;
    private Long quantity;

    public FilterResponseData(
            Long productId,
            Long clientId,
            String productName,
            String barcode,
            Double mrp,
            Long quantity
    ) {
        this.productId = productId;
        this.clientId = clientId;
        this.productName = productName;
        this.barcode = barcode;
        this.mrp = mrp;
        this.quantity = quantity;
    }
}
