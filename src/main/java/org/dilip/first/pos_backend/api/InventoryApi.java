package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.InventoryDao;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.flow.InventoryFlow;
import org.dilip.first.pos_backend.model.inventory.InventoryFilterResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private InventoryFlow inventoryFlow;

    public InventoryEntity getById(Long id) {
        InventoryEntity inventory = inventoryDao.findById(InventoryEntity.class, id);
        if (inventory == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Inventory not found " + id);
        }
        return inventory;
    }

    public InventoryEntity create(Long productId, Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid quantity " + quantity);
        }
        return inventoryFlow.create(productId, quantity);
    }

    public InventoryEntity updateQuantity(Long productId, Long newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Quantity should be greater than 0 " + newQuantity);
        }

        InventoryEntity inventory = inventoryDao.findByProductId(productId);
        if (inventory == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Inventory not found for product " + productId);
        }

        inventory.setQuantity(newQuantity);
        return inventoryDao.save(inventory);
    }

    public void reduce(String barcode, Long quantity) {
        inventoryFlow.reduceByBarcode(barcode, quantity);
    }

    public List<InventoryEntity> getAll(int page, int size) {
        return inventoryDao.findAll(InventoryEntity.class, page, size);
    }

    public List<InventoryFilterResponseData> filter(Long productId, String barcode,
                                                    String name, int page, int size) {
        return inventoryDao.filter(productId, barcode, name, page, size);
    }

    public String uploadInventoryRow(String barcode, Long quantity) {
        return inventoryFlow.uploadInventoryRow(barcode, quantity);
    }

    public void validateAvailability(String barcode, Long quantity) {
        inventoryFlow.validateAvailability(barcode, quantity);
    }

}
