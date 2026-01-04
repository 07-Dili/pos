package org.dilip.first.pos_backend.model.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateForm {

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 0, message = "Quantity cannot be negative")
    private Long quantity;
}
