package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.error.InventoryUploadError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.dilip.first.pos_backend.util.helper.IntegerUtil.parseLong;

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

    public List<InventoryUploadError> uploadInventory(List<String[]> rows) {

        List<String> barcodes = rows.stream()
                .map(r -> r.length > 0 ? r[0].trim() : null)
                .filter(b -> b != null && !b.isBlank())
                .collect(Collectors.toList());

        List<InventoryUploadError> errors = new ArrayList<>();

        List<ProductEntity> products = productApi.getAllWithoutPagination(barcodes);
        Map<String, ProductEntity> productMap = products.stream()
                .collect(Collectors.toMap(ProductEntity::getBarcode, p -> p));

        int lineNumber = 1;

        for (String[] row : rows) {

            String barcode = row.length > 0 ? row[0].trim() : null;
            Long quantity = row.length > 1 ? parseLong(row[1]) : null;

            if (barcode == null || barcode.isBlank()) {
                errors.add(new InventoryUploadError(lineNumber, barcode, "Barcode is empty"));
                lineNumber++;
                continue;
            }

            if (quantity == null || quantity <= 0) {
                errors.add(new InventoryUploadError(lineNumber, barcode, "Invalid quantity: " + quantity));
                lineNumber++;
                continue;
            }

            ProductEntity product = productMap.get(barcode);
            if (product == null) {
                errors.add(new InventoryUploadError(lineNumber, barcode, "Product not found for barcode: " + barcode));
                lineNumber++;
                continue;
            }
            inventoryApi.uploadInventoryRow(product, quantity);

            lineNumber++;
        }

        return errors;
    }
}
