package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.flow.InventoryFlow;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertInventoryEntityToData;
import static org.dilip.first.pos_backend.util.helper.ExceptionBuilder.buildInventoryUploadException;

@Component
public class InventoryDto {

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InventoryFlow inventoryFlow ;

    public void uploadInventory(MultipartFile file) {

        List<String[]> rows = TsvUtil.parse(file, 5000);

        List<InventoryUploadError> errors = inventoryFlow.uploadInventory(rows);

        if (!errors.isEmpty()) {
            throw buildInventoryUploadException(errors);
        }
    }


    public InventoryData create(InventoryCreateForm form) {
        InventoryEntity entity = inventoryFlow.create(form.getProductId(), form.getQuantity());
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

    public List<InventoryFilterResponseData> filter(InventorySearchForm form) {

        String barcode = StringUtil.normalizeToLowerCase(form.getBarcode());
        String name = StringUtil.normalizeToLowerCase(form.getName());
        Long productId = form.getProductId();
        int page = 0;
        int size = 10;

        if (barcode != null && barcode.isBlank()) barcode = null;
        if (name != null && name.isBlank()) name = null;

        return inventoryApi.filter(productId, barcode, name, page, size);
    }
}
