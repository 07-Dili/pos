package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.InventoryDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.data.FilterResponseData;
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
    private ProductDao productDao;

    public InventoryEntity getById(Long id) {
        return inventoryDao.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventory not found " + id));
    }

    public InventoryEntity create(Long productId, Long quantity) {

        if (quantity == null || quantity < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid quantity " + quantity);
        }

        ProductEntity product = productDao.findById(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found " + productId));

        if (inventoryDao.findByProductId(productId).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Inventory already exists for product " + productId);
        }

        InventoryEntity inventory = new InventoryEntity();
        inventory.setProduct(product);
        inventory.setQuantity(quantity);

        return inventoryDao.save(inventory);
    }

    public InventoryEntity updateQuantity(Long productId, Long newQuantity) {

        if (newQuantity < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Quantity cannot be negative " + newQuantity);
        }

        InventoryEntity inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventory not found for product " + productId));

        inventory.setQuantity(newQuantity);
        return inventoryDao.save(inventory);
    }

    public void reduce(Long productId, Long quantity) {

        InventoryEntity inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventory not found for product " + productId));

        if (inventory.getQuantity() < quantity) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient inventory " + inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryDao.save(inventory);
    }

    public List<InventoryEntity> getAll(int page, int size) {
        int offset = page * size;
        return inventoryDao.findAll(size, offset);
    }

    public List<FilterResponseData> filter(
            Long productId,
            String barcode,
            String name,
            int page,
            int size) {

        int offset = page * size;
        return inventoryDao.filter(productId, barcode, name, size, offset);
    }




    public void uploadInventoryRow(String barcode, Long quantity) {

        if (barcode == null || barcode.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Barcode is required");
        }

        if (quantity == null || quantity <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid quantity " + quantity);
        }

        ProductEntity product = productDao.findByBarcode(barcode);
        if (product == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Product not found for barcode: " + barcode);
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
}
