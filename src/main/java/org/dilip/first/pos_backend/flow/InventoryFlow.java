package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.dao.InventoryDao;
import org.dilip.first.pos_backend.dao.ProductDao;
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
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    public InventoryEntity create(Long productId, Long quantity) {

        ProductEntity product = productDao.findById(productId);
        if (product == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Product not found " + productId);
        }

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

    public boolean uploadInventoryRow(String barcode, Long quantity) {

        ProductEntity product = productDao.findByBarcode(barcode);
        if (product == null) {
            return false;
        }

        InventoryEntity inventory = inventoryDao.findByProductId(product.getId());

        if (inventory == null) {
            inventory = new InventoryEntity();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        }

        inventoryDao.save(inventory);
        return true;
    }

}

