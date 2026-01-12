package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.flow.ProductFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductFlow productFlow;

    public ProductEntity getByBarcode(String barcode) {
        ProductEntity product = productDao.findByBarcode(barcode);
        if (product == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Product not found for barcode: " + barcode);
        }
        return product;
    }

    public ProductEntity getById(Long id) {
        ProductEntity product = productDao.findById(ProductEntity.class, id);
        if (product == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Product not found with id: " + id);
        }
        return product;
    }

    public ProductEntity create(Long clientId, String name, String barcode, Double mrp) {


        if (productDao.findByBarcode(barcode) != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Barcode already exists: " + barcode);
        }

        return productFlow.create(clientId, name, barcode, mrp);
    }

    public ProductEntity update(Long id, Long clientId, String name, Double mrp, String barcode) {

        ProductEntity product = productDao.findById(ProductEntity.class,id);
        if (product == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Product not found");
        }

        ProductEntity existing = productDao.findByBarcode(barcode);
        if (existing != null && !existing.getId().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Barcode already exists: " + barcode);
        }

        return productFlow.update(product, clientId, name, mrp, barcode);
    }

    public List<ProductEntity> getAll(int page, int size) {
        return productDao.findAll(ProductEntity.class,page, size);
    }

    public List<ProductEntity> search(Long id, Long clientId, String name, String barcode, int page, int size) {
        return productDao.search(id, clientId, name, barcode, page, size);
    }
}
