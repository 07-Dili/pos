package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.InventoryDao;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
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

    public InventoryEntity getById(Long id) {
        InventoryEntity inventory = inventoryDao.findById(InventoryEntity.class, id);
        if (inventory == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Inventory not found " + id);
        }
        return inventory;
    }

    public List<InventoryEntity> getAll(int page, int size) {
        return inventoryDao.findAll(InventoryEntity.class, page, size);
    }

    public List<InventoryFilterResponseData> filter(Long productId, String barcode,
                                                    String name, int page, int size) {
        return inventoryDao.filter(productId, barcode, name, page, size);
    }

    public void uploadInventoryRow(ProductEntity product,Long quantity) {
        InventoryEntity inventory = inventoryDao.findByProductId(product.getId());

        if (inventory == null) {
            inventory = new InventoryEntity();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        }
        inventoryDao.save(inventory);
    }

    public InventoryEntity create(ProductEntity product,Long productId, Long quantity) {

        InventoryEntity inventory = inventoryDao.findByProductId(productId);

        if (inventory == null) {
            inventory = new InventoryEntity();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        }

        return inventoryDao.save(inventory);
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

    public InventoryEntity findByProductId(Long productId) {
        return inventoryDao.findByProductId(productId);
    }

    public void reduce(InventoryEntity inventory, Long quantity) {
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryDao.save(inventory);
    }
}
