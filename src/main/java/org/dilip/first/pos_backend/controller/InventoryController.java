package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.InventoryDto;
import org.dilip.first.pos_backend.model.data.InventoryData;
import org.dilip.first.pos_backend.model.form.InventoryUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.dilip.first.pos_backend.model.data.ProductInventoryData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @GetMapping("/{id}")
    public InventoryData getById(@PathVariable Long id) {
        return inventoryDto.getById(id);
    }

    @GetMapping("/filter")
    public Page<InventoryData> filter(@RequestParam(required = false) String barcode,
            @RequestParam(required = false) String name, Pageable pageable) {
        return inventoryDto.filter(barcode, name, pageable);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadInventory(@RequestParam MultipartFile file) {
        inventoryDto.uploadInventory(file);
    }

    @PutMapping
    public InventoryData update(@Valid @RequestBody InventoryUpdateForm form) {
        return inventoryDto.update(form);
    }

    @GetMapping
    public Page<ProductInventoryData> getAll(
            @PageableDefault(size = 10, sort = "product.name", direction = Sort.Direction.ASC) Pageable pageable) {
        return inventoryDto.getAll(pageable);
    }
}
