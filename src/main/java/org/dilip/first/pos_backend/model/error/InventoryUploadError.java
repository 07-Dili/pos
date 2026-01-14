package org.dilip.first.pos_backend.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryUploadError {
    private int lineNumber;
    private String barcode;
    private String errorMessage;
}
