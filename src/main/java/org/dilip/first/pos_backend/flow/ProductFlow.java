package org.dilip.first.pos_backend.flow;

import jakarta.transaction.Transactional;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.api.ProductApi;
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
    private ClientApi clientApi;

    @Autowired
    private ProductApi productApi;

    public ProductEntity create(Long clientId, String name, String barcode, Double mrp) {

        validateClientExist(clientId);

        ProductEntity existing = productApi.getByBarcode(barcode);

        if (existing != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,buildDuplicateBarcodeMessage(existing));
        }

        return productApi.create(clientId, name, barcode, mrp);
    }

    public ProductEntity update(Long id, Long clientId, String name, Double mrp, String barcode) {

        validateClientExist(clientId);

        ProductEntity product = productApi.getById(id);
        if (product == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Product not found");
        }

        ProductEntity existing = productApi.getByBarcode(barcode);
        if (existing != null && !existing.getId().equals(id)) {
            throw new ApiException( HttpStatus.BAD_REQUEST, buildDuplicateBarcodeMessage(existing));
        }

        return productApi.update(product,clientId,name,mrp,barcode);
    }

    private void validateClientExist(Long clientId) {
        if (clientApi.getById(clientId) == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client not found with id: " + clientId);
        }
    }

    public String buildDuplicateBarcodeMessage(ProductEntity existing) {

        ClientEntity client = clientApi.getById(existing.getClientId());

        String clientName = (client != null) ? client.getName() : String.valueOf(existing.getClientId());

        return "Product barcode already exists for client: " + clientName;
    }
}

