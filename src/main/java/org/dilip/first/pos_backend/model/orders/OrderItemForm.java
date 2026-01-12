package org.dilip.first.pos_backend.model.orders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String barcode;

    @NotNull
    @Positive
    private Long quantity;

    @NotNull
    @Positive
    private Double sellingPrice;
}
