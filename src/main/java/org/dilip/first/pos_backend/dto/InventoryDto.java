package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.error.InventoryUploadError;
import org.dilip.first.pos_backend.model.inventory.InventoryFilterResponseData;
import org.dilip.first.pos_backend.model.inventory.InventoryData;
import org.dilip.first.pos_backend.model.products.ProductInventoryData;
import org.dilip.first.pos_backend.model.inventory.InventorySearchForm;
import org.dilip.first.pos_backend.model.inventory.InventoryUpdateForm;
import org.dilip.first.pos_backend.model.inventory.InventoryCreateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.dilip.first.pos_backend.util.helper.TsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertInventoryEntityToData;
import static org.dilip.first.pos_backend.util.helper.IntegerUtil.parseLong;

@Component
public class InventoryDto {

    @Autowired
    private InventoryApi inventoryApi;

    public void uploadInventory(MultipartFile file) {

        List<String[]> rows = TsvUtil.parse(file, 5000);

        List<InventoryUploadError> errors = new ArrayList<>();

        int lineNumber = 1;

        for (String[] row : rows) {

            String barcode = row.length > 0 ? row[0].trim() : null;
            Long quantity = row.length > 1 ? parseLong(row[1]) : null;

            boolean success = inventoryApi.uploadInventoryRow(barcode, quantity);

            if (!success) {
                errors.add(new InventoryUploadError(lineNumber, barcode));
            }

            lineNumber++;
        }

        if (!errors.isEmpty()) {
            throw buildInventoryUploadException(errors);
        }
    }

    private ApiException buildInventoryUploadException(List<InventoryUploadError> errors) {

        String message = errors.stream()
                .map(e -> "line " + e.getLineNumber() + " (barcode: " + e.getBarcode() + ")")
                .collect(Collectors.joining(", "));

        return new ApiException(
                HttpStatus.BAD_REQUEST,
                "Inventory upload failed for: " + message
        );
    }



    public InventoryData create(InventoryCreateForm form) {
        InventoryEntity entity = inventoryApi.create(form.getProductId(), form.getQuantity());
        return convertInventoryEntityToData(entity);
    }

    public InventoryData update(InventoryUpdateForm form) {
        InventoryEntity entity = inventoryApi.updateQuantity(form.getProductId(), form.getQuantity());
        return convertInventoryEntityToData(entity);
    }

    public InventoryData getById(Long id) {
        return convertInventoryEntityToData(inventoryApi.getById(id));
    }

    public List<ProductInventoryData> getAll(int page, int size) {
        return inventoryApi.getAll(page, size)
                .stream()
                .map(EntityToData::convertInventoryEntityToCompleteProductData)
                .toList();
    }


    public List<InventoryFilterResponseData> filter( InventorySearchForm form) {

        String barcode=StringUtil.normalizeToLowerCase(form.getBarcode());
        String name= StringUtil.normalizeToLowerCase(form.getName());
        Long productId=form.getProductId();
        int page = 0;
        int size = 10;
        if (barcode != null && barcode.isBlank()) barcode = null;
        if (name != null && name.isBlank()) name = null;

        return inventoryApi.filter(productId, barcode, name, page, size);
    }

}
