package org.dilip.first.pos_backend.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateForm {

    @NotNull(message = "Client id is required")
    private Long clientId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product barcode is required")
    private String barcode;

    @NotNull(message = "MRP is required")
    @Positive(message = "MRP must be greater than zero")
    private Double mrp;
}
