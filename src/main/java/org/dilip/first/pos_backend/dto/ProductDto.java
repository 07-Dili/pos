package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.flow.ProductFlow;
import org.dilip.first.pos_backend.model.error.ProductUploadError;
import org.dilip.first.pos_backend.model.products.ProductData;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.dilip.first.pos_backend.model.products.ProductSearchForm;
import org.dilip.first.pos_backend.model.products.ProductUpdateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.conversion.ProductTsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertProductEntityToData;
import static org.dilip.first.pos_backend.util.helper.ExceptionBuilder.buildProductUploadException;

@Component
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ProductFlow productFlow;

    public ProductData create(ProductForm form) {
        ProductEntity entity = productFlow.create( form.getClientId(), form.getName().trim().toLowerCase(),
                form.getBarcode().trim().toLowerCase(),form.getMrp()
        );
        return convertProductEntityToData(entity);
    }

    public List<ProductData> search(ProductSearchForm form) {
        List<ProductEntity> entities = productApi.search(form.getId(), form.getClientId(), form.getName(),
                form.getBarcode(),
                form.getPage(),
                form.getSize()
        );

        return entities.stream().map(EntityToData::convertProductEntityToData).toList();
    }

    public ProductData update(Long id, ProductUpdateForm form) {
        ProductEntity entity = productFlow.update(id, form.getClientId(), form.getName(), form.getMrp(), form.getBarcode());
        return convertProductEntityToData(entity);
    }

    public List<ProductData> getAll(int page, int size) {
        return productApi.getAll(page, size)
                .stream()
                .map(EntityToData::convertProductEntityToData)
                .toList();
    }

    public void uploadProductMaster(MultipartFile file) {

        List<ProductForm> forms = ProductTsvParser.parse(file);

        List<ProductUploadError> errors = productFlow.uploadProducts(forms);

        if (!errors.isEmpty()) {
            throw buildProductUploadException(errors);
        }
    }

}
