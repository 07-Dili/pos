package org.dilip.first.pos_backend.flow;

import jakarta.transaction.Transactional;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.error.ProductUploadError;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<ProductUploadError> uploadProducts(List<ProductForm> forms) {

        List<ProductUploadError> errors = new ArrayList<>();

        Set<Long> clientIds = forms.stream()
                .map(ProductForm::getClientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> barcodes = forms.stream()
                .map(ProductForm::getBarcode)
                .filter(b -> b != null && !b.isBlank())
                .map(b -> b.trim().toLowerCase())
                .collect(Collectors.toSet());

        List<ClientEntity> clients = clientApi.getAllByIds(clientIds);

        Map<Long, ClientEntity> clientMap = clients.stream()
                .collect(Collectors.toMap(ClientEntity::getId, c -> c));

        List<ProductEntity> existingProducts = productApi.getAllWithoutPagination(new ArrayList<>(barcodes));

        Map<String, ProductEntity> productMap = existingProducts.stream()
                .collect(Collectors.toMap(
                        ProductEntity::getBarcode,
                        p -> p,
                        (p1, p2) -> p1
                ));

        int lineNumber = 1;

        for (ProductForm form : forms) {

            String barcode = form.getBarcode();
            String name = form.getName();
            Long clientId = form.getClientId();
            Double mrp = form.getMrp();

            if (barcode == null || barcode.isBlank()) {
                errors.add(new ProductUploadError(lineNumber, barcode, "Barcode is empty"));
                lineNumber++;
                continue;
            }

            if (name == null || name.isBlank()) {
                errors.add(new ProductUploadError(lineNumber, barcode, "Product name is empty"));
                lineNumber++;
                continue;
            }

            if (mrp == null || mrp <= 0) {
                errors.add(new ProductUploadError(lineNumber, barcode, "Invalid MRP"));
                lineNumber++;
                continue;
            }

            if (!clientMap.containsKey(clientId)) {
                errors.add(new ProductUploadError(lineNumber, barcode, "Client not found with id: " + clientId));
                lineNumber++;
                continue;
            }

            String normalizedBarcode = barcode.trim().toLowerCase();

            if (productMap.containsKey(normalizedBarcode)) {
                errors.add(new ProductUploadError( lineNumber, barcode, buildDuplicateBarcodeMessage(productMap.get(normalizedBarcode))));
                lineNumber++;
                continue;
            }

            productApi.create( clientId, name.trim().toLowerCase(), normalizedBarcode, mrp);

            lineNumber++;
        }

        return errors;
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

