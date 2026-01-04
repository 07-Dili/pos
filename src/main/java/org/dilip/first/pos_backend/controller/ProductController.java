package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.ProductDto;
import org.dilip.first.pos_backend.model.data.ProductData;
import org.dilip.first.pos_backend.model.form.ProductForm;
import org.dilip.first.pos_backend.model.form.ProductUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductData> getAll(Pageable pageable) {
        return productDto.getAll(pageable);
    }

    @GetMapping("/search")
    public List<ProductData> searchByName(@RequestParam String name) {
        return productDto.searchByName(name);
    }

    @PutMapping("/{id}")
    public ProductData update(@PathVariable Long id, @Valid @RequestBody ProductUpdateForm form) {
        return productDto.update(id, form);
    }

    @GetMapping("/filter")
    public Page<ProductData> filter(@RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String name, Pageable pageable) {
        return productDto.filter(clientId, name, pageable);
    }

    @PostMapping(value = "/products/upload", consumes = "multipart/form-data")
    public void uploadProductMaster(@RequestParam("file") MultipartFile file) {
        productDto.uploadProductMaster(file);
    }

}
