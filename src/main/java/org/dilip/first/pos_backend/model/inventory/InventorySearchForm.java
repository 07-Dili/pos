package org.dilip.first.pos_backend.model.inventory;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventorySearchForm {
    private Long productId;
    private String barcode;
    private String name;
    private Integer page = 0;
    private Integer size = 10;
}
