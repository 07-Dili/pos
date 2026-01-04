package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.dao.InventoryDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    public InventoryEntity getById(Long id) {
        return inventoryDao.findById(id).orElseThrow(() -> new ApiException(404, "Inventory not found "+id));
    }

    public void reduce(Long productId, Long quantity) {

        InventoryEntity inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new ApiException(404, "Inventory not found for product "+productId));

        if (inventory.getQuantity() < quantity) {
            throw new ApiException(403, "Insufficient inventory "+inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);

        inventoryDao.save(inventory);
    }

    public Page<InventoryEntity> filter(String barcode, String name, Pageable pageable) {
        return inventoryDao.filter(barcode, name, pageable);
    }

    public Page<InventoryEntity> getAll(Pageable pageable) {
        return inventoryDao.findAll(pageable);
    }

    public void uploadInventoryRow(String barcode, Long quantity) {

        if (barcode == null || barcode.isBlank()) {
            throw new ApiException(400, "Barcode is required");
        }

        if (quantity == null) {
            throw new ApiException(400, "Quantity is required");
        }

        if (quantity <= 0) {
            throw new ApiException(400, "Quantity cannot be less than or equal to zero");
        }

        ProductEntity product = productDao.findByBarcode(barcode);
        if (product == null) {
            throw new ApiException(404, "Product not found for barcode: " + barcode);
        }

        InventoryEntity inventory = inventoryDao.findByProductId(product.getId())
                .orElseGet(() -> {
                    InventoryEntity i = new InventoryEntity();
                    i.setProduct(product);
                    return i;
                });

        inventory.setQuantity(quantity);
        inventoryDao.save(inventory);
    }

    public InventoryEntity updateQuantity(Long productId, Long newQuantity) {

        if (newQuantity < 0) {
            throw new ApiException(400, "Quantity cannot be negative "+newQuantity);
        }

        InventoryEntity inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new ApiException(404, "Inventory not found for product "+productId));

        inventory.setQuantity(newQuantity);
        return inventoryDao.save(inventory);
    }
}
