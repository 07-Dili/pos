package org.dilip.first.pos_backend.model.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {

    private Long id;
    private Long clientId;
    private String name;
    private String barcode;
    private Double mrp;
}

