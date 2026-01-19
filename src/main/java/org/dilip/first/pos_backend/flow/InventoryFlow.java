package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class InventoryFlow {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    public InventoryEntity create(Long productId, Long quantity) {

        if (quantity == null || quantity <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid quantity " + quantity);
        }

        ProductEntity product = productApi.getById(productId);

        if (product == null) {
            throw new RuntimeException("Product not found " + productId);
        }

        return inventoryApi.create(product,productId, quantity);
    }

    public String uploadInventoryRow(String barcode, Long quantity) {

        if (barcode == null || barcode.isBlank()) {
            return "Barcode is empty";
        }

        if (quantity == null || quantity <= 0) {
            return "Invalid quantity: " + quantity;
        }

        ProductEntity product = productApi.getByBarcode(barcode);
        if (product == null) {
            return "Product not found for barcode: " + barcode;
        }

        inventoryApi.uploadInventoryRow(product,quantity);
        return null;

    }
}
