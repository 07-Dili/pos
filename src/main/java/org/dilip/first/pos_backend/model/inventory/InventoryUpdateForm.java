package org.dilip.first.pos_backend.model.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateForm {

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 1, message = "Quantity cannot be negative")
    private Long quantity;
}
