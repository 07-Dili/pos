package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.model.data.FilterResponseData;
import org.dilip.first.pos_backend.model.data.InventoryData;
import org.dilip.first.pos_backend.model.data.ProductInventoryData;
import org.dilip.first.pos_backend.model.form.InventoryUpdateForm;
import org.dilip.first.pos_backend.model.form.InventoryCreateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.helper.TsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertInventoryEntityToData;
import static org.dilip.first.pos_backend.util.helper.IntegerUtil.parseLong;

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


    public List<FilterResponseData> filter(
            Long productId,
            String barcode,
            String name,
            int page,
            int size) {

        if (barcode != null && barcode.isBlank()) barcode = null;
        if (name != null && name.isBlank()) name = null;

        return inventoryApi.filter(productId, barcode, name, page, size);
    }

}
