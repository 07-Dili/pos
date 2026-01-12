package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.ProductDto;
import org.dilip.first.pos_backend.model.products.ProductData;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.dilip.first.pos_backend.model.products.ProductSearchForm;
import org.dilip.first.pos_backend.model.products.ProductUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @PostMapping
    public ProductData create(@Valid @RequestBody ProductForm form) {
        return productDto.create(form);
    }

    @GetMapping
    public List<ProductData> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return productDto.getAll(page, size);
    }

    @PutMapping("/{id}")
    public ProductData update(@PathVariable Long id, @Valid @RequestBody ProductUpdateForm form) {
        return productDto.update(id, form);
    }

    @GetMapping("/search")
    public List<ProductData> search(ProductSearchForm form) {
        return productDto.search(form);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadProductMaster(@RequestParam("file") MultipartFile file) {
        productDto.uploadProductMaster(file);
    }
}
