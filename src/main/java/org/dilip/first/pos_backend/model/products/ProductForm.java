package org.dilip.first.pos_backend.model.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

    @NotNull
    private Long clientId;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 1, max = 10)
    private String barcode;

    @NotNull
    @DecimalMin(value = "1", message = "MRP must be greater than 0")
    private Double mrp;
}

