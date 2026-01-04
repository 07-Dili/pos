package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.model.data.InventoryData;
import org.dilip.first.pos_backend.model.form.InventoryUpdateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.helper.TsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.dilip.first.pos_backend.model.data.ProductInventoryData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertInventoryEntityToData;

@Component
public class InventoryDto {

    @Autowired
    private InventoryApi inventoryApi;

    public void uploadInventory(MultipartFile file) {

        List<String[]> rows = TsvUtil.parse(file, 5000);
        for (String[] row : rows) {
            String barcode = row.length > 0 ? row[0].trim() : null;
            Long quantity = row.length > 1 ? parseLong(row[1]) : null;
            inventoryApi.uploadInventoryRow(barcode, quantity);
        }
    }
    public InventoryData update(InventoryUpdateForm form) {

        InventoryEntity entity = inventoryApi.updateQuantity( form.getProductId(), form.getQuantity() );
        return convertInventoryEntityToData(entity);
    }

    public Page<ProductInventoryData> getAll(Pageable pageable) {
        return inventoryApi.getAll(pageable).map(EntityToData::convertInventoryEntityToCompleteProductData);
    }

    public InventoryData getById(Long id) {
        return convertInventoryEntityToData(inventoryApi.getById(id));
    }

    public Page<InventoryData> filter(String barcode, String name, Pageable pageable) {
        return inventoryApi.filter(barcode, name, pageable).map(EntityToData::convertInventoryEntityToData);
    }

    private Long parseLong(String value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
