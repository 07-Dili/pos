package org.dilip.first.pos_backend.model.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreateForm {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Long quantity;
}
