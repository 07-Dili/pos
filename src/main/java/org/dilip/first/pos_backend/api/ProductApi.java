package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ClientDao clientDao;

    public ProductEntity getByBarcode(String barcode) {

        ProductEntity product = productDao.findByBarcode(barcode);

        if (product == null) {
            throw new ApiException(404, "Product not found for barcode: " + barcode);
        }

        return product;
    }

    public ProductEntity create(Long clientId, String name, String barcode, Double mrp) {

        if (!clientDao.existsById(clientId)) {
            throw new ApiException(404, "Client does not exist " + clientId);
        }

        if (productDao.findByBarcode(barcode) != null) {
            throw new ApiException(409, "Barcode already exists " + barcode);
        }

        ProductEntity entity = new ProductEntity();
        entity.setClientId(clientId);
        entity.setName(name);
        entity.setBarcode(barcode);
        entity.setMrp(mrp);

        return productDao.save(entity);
    }

    public List<ProductEntity> search(Long clientId, String name, String barcode, int page, int size) {

        int offset = page * size;
        return productDao.search(clientId, name, barcode, size, offset);
    }

    public ProductEntity update(Long id, Long clientId, String name, Double mrp, String barcode) {

        ProductEntity product = productDao.findById(id)
                .orElseThrow(() -> new ApiException(404, "Product not found " + id));

        clientDao.findById(clientId)
                .orElseThrow(() -> new ApiException(404, "Client does not exist " + clientId));

        ProductEntity existingByBarcode = productDao.findByBarcode(barcode);

        if (existingByBarcode != null && !existingByBarcode.getId().equals(id)) {
            throw new ApiException(409, "Barcode already exists " + barcode);
        }

        product.setClientId(clientId);
        product.setName(name);
        product.setMrp(mrp);
        product.setBarcode(barcode);

        return productDao.save(product);
    }

    public List<ProductEntity> getAll(int page, int size) {

        int offset = page * size;
        return productDao.findAllWithPagination(size, offset);
    }
}
