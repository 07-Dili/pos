package org.dilip.first.pos_backend.util.helper;

import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.error.InventoryUploadError;
import org.dilip.first.pos_backend.model.error.ProductUploadError;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ExceptionBuilder {

    public static  ApiException buildInventoryUploadException(List<InventoryUploadError> errors) {

        String message = errors.stream()
                .map(e -> "line " + e.getLineNumber() +
                        " (barcode: " + e.getBarcode() + ") - " +
                        e.getErrorMessage())
                .collect(Collectors.joining(", "));

        return new ApiException( HttpStatus.BAD_REQUEST, "Inventory upload failed for: " + message);
    }

    public static ApiException buildProductUploadException(List<ProductUploadError> errors) {

        String message = errors.stream()
                .map(e ->
                        "line " + e.getLineNumber()
                                + " (barcode: " + e.getBarcode() + ") - "
                                + e.getMessage()
                )
                .collect(Collectors.joining(", "));

        return new ApiException(HttpStatus.BAD_REQUEST, "Product upload failed for: " + message);
    }
}
