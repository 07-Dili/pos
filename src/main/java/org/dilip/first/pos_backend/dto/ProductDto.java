package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.products.ProductData;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.dilip.first.pos_backend.model.products.ProductSearchForm;
import org.dilip.first.pos_backend.model.products.ProductUpdateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.conversion.ProductTsvParser;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertProductEntityToData;

@Component
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    public ProductData create(ProductForm form) {

        ProductEntity entity = productApi.create(form.getClientId(), form.getName().trim().toLowerCase(), form.getBarcode().trim().toLowerCase(), form.getMrp());
        return convertProductEntityToData(entity);
    }

    public List<ProductData> search(ProductSearchForm form) {

        Long id = form.getId();
        Long clientId = form.getClientId();
        String name = StringUtil.normalizeToLowerCase(form.getName());
        String barcode = StringUtil.normalizeToLowerCase(form.getBarcode());
        int page = form.getPage();
        int size = form.getSize();

        List<ProductEntity> entities = productApi.search(id, clientId, name, barcode, page, size);

        return entities.stream()
                .map(EntityToData::convertProductEntityToData)
                .toList();
    }


    public ProductData update(Long id, ProductUpdateForm form) {

        Long clientId = form.getClientId();
        String name = StringUtil.normalizeToLowerCase(form.getName());
        String barcode = StringUtil.normalizeToLowerCase(form.getBarcode());
        Double mrp = form.getMrp();

        ProductEntity entity = productApi.update(id, clientId, name, mrp, barcode);
        return convertProductEntityToData(entity);
    }

    public List<ProductData> getAll(int page, int size) {

        List<ProductEntity> entities = productApi.getAll(page, size);
        return entities.stream().map(EntityToData::convertProductEntityToData).toList();
    }

    public void uploadProductMaster(MultipartFile file) {

        List<ProductForm> forms = ProductTsvParser.parse(file);

        for (ProductForm form : forms) {
            create(form);
        }
    }
}
