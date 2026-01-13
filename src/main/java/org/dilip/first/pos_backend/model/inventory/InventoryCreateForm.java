package org.dilip.first.pos_backend.model.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreateForm {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    @Size(max = 256,message = "Maximum 19 digits is supported for quantity")
    private Long quantity;
}
