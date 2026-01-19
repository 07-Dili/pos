package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
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

    public ProductEntity create(Long clientId, String name, String barcode, Double mrp) {

        ProductEntity entity = new ProductEntity();
        entity.setClientId(clientId);
        entity.setName(name);
        entity.setBarcode(barcode);
        entity.setMrp(mrp);

        return productDao.save(entity);
    }

    public ProductEntity update(ProductEntity product,Long clientId, String name, Double mrp, String barcode) {

        product.setClientId(clientId);
        product.setName(name);
        product.setMrp(mrp);
        product.setBarcode(barcode);

        return productDao.save(product);
    }

    public ProductEntity getById(Long id) {
        ProductEntity product = productDao.findById(ProductEntity.class, id);
        if (product == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Product not found with id: " + id);
        }
        return product;
    }

    public List<ProductEntity> getAll(int page, int size) {
        return productDao.findAll(ProductEntity.class, page, size);
    }

    public List<ProductEntity> search(Long id, Long clientId, String name, String barcode, int page, int size) {
        return productDao.search(id, clientId, name, barcode, page, size);
    }

    //we need this method because we dont need any exception to throw for update method
    public ProductEntity getByBarcode(String barcode) {
        return productDao.findByBarcode(barcode);
    }
}
