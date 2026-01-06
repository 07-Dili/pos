package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.InventoryDto;
import org.dilip.first.pos_backend.model.data.FilterResponseData;
import org.dilip.first.pos_backend.model.data.InventoryData;
import org.dilip.first.pos_backend.model.data.ProductInventoryData;
import org.dilip.first.pos_backend.model.form.InventoryUpdateForm;
import org.dilip.first.pos_backend.model.form.InventoryCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public List<FilterResponseData> filter(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return inventoryDto.filter(productId, barcode, name, page, size);
    }




    @GetMapping
    public List<ProductInventoryData> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return inventoryDto.getAll(page, size);
    }

    @PostMapping
    public InventoryData create(@Valid @RequestBody InventoryCreateForm form) {
        return inventoryDto.create(form);
    }

    @PutMapping
    public InventoryData update(@Valid @RequestBody InventoryUpdateForm form) {
        return inventoryDto.update(form);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadInventory(@RequestParam("file") MultipartFile file) {
        inventoryDto.uploadInventory(file);
    }
}
