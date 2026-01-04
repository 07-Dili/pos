package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

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
            throw new ApiException(404, "Client does not exist "+clientId);
        }

        if (productDao.findByBarcode(barcode) != null) {
            throw new ApiException(409, "Barcode already exists "+barcode);
        }

        ProductEntity entity = new ProductEntity();
        entity.setClientId(clientId);
        entity.setName(name);
        entity.setBarcode(barcode);
        entity.setMrp(mrp);

        return productDao.save(entity);
    }

    public List<ProductEntity> searchByName(String name) {
        return productDao.findByNameContainingIgnoreCase(name);
    }

    public Page<ProductEntity> filter(Long clientId, String name, Pageable pageable) {
        return productDao.filter(clientId, name, pageable);
    }

    public ProductEntity update(Long id, Long clientId, String name, Double mrp) {

        ProductEntity product = productDao.findById(id)
                .orElseThrow(() -> new ApiException(404, "Product not found "+id));

        ClientEntity client = clientDao.findById(clientId)
                .orElseThrow(() -> new ApiException(404, "Client does not exist "+clientId));

        product.setClientId(clientId);
        product.setName(name);
        product.setMrp(mrp);

        return productDao.save(product);
    }

    public org.springframework.data.domain.Page<ProductEntity> getAll(Pageable pageable) {
        return productDao.findAll(pageable);
    }
}
