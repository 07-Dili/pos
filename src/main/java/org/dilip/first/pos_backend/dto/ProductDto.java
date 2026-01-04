package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.data.ProductData;
import org.dilip.first.pos_backend.model.form.ProductForm;
import org.dilip.first.pos_backend.model.form.ProductUpdateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.conversion.ProductTsvParser;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertProductEntityToData;

@Component
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    public ProductData create(ProductForm form) {

        ProductEntity entity = productApi.create(
                form.getClientId(),
                form.getName().trim().toLowerCase(),
                form.getBarcode().trim().toLowerCase(),
                form.getMrp());

        return convertProductEntityToData(entity);
    }

    public ProductData update(Long id, ProductUpdateForm form) {

        Long clientId = form.getClientId();
        String name = StringUtil.normalizeToLowerCase(form.getName());
        Double mrp = form.getMrp();

        ProductEntity entity = productApi.update(id, clientId, name, mrp);
        return convertProductEntityToData(entity);
    }

    public List<ProductData> searchByName(String name) {
        return productApi.searchByName(name).stream().map(EntityToData::convertProductEntityToData).toList();
    }

    public Page<ProductData> filter(Long clientId, String name, Pageable pageable) {
        return productApi.filter(clientId, name, pageable).map(EntityToData::convertProductEntityToData);
    }

    public Page<ProductData> getAll(Pageable pageable) {
        return productApi.getAll(pageable).map(EntityToData::convertProductEntityToData);
    }

    public void uploadProductMaster(MultipartFile file) {

        List<ProductForm> forms = ProductTsvParser.parse(file);

        if (forms.size() > 5000) {
            throw new ApiException(400, "Maximum 5000 rows allowed. Found: " + forms.size());
        }

        for (ProductForm form : forms) {
            create(form);
        }
    }


}
