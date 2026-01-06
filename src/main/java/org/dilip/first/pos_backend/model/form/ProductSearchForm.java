package org.dilip.first.pos_backend.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchForm {

    private Long clientId;
    private Long id;
    private String name;
    private String barcode;
    private Integer page = 0;
    private Integer size = 10;
}
