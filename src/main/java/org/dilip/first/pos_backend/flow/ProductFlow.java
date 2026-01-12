package org.dilip.first.pos_backend.flow;

import jakarta.transaction.Transactional;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.dao.ProductDao;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class ProductFlow {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ProductDao productDao;

    public ProductEntity create(Long clientId, String name, String barcode, Double mrp) {

        if (clientDao.findById(ClientEntity.class,clientId) == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId);
        }

        ProductEntity entity = new ProductEntity();
        entity.setClientId(clientId);
        entity.setName(name);
        entity.setBarcode(barcode);
        entity.setMrp(mrp);

        return productDao.save(entity);
    }

    public ProductEntity update(ProductEntity product, Long clientId, String name, Double mrp, String barcode) {

        if (clientDao.findById(ClientEntity.class,clientId) == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId);
        }

        product.setClientId(clientId);
        product.setName(name);
        product.setMrp(mrp);
        product.setBarcode(barcode);

        return productDao.save(product);
    }

}

