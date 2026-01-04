package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.ClientDto;
import org.dilip.first.pos_backend.model.data.ClientData;
import org.dilip.first.pos_backend.model.form.ClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @PostMapping
    public ClientData create(@Valid @RequestBody ClientForm form) {
        return clientDto.create(form);
    }

    @GetMapping("/{id}")
    public ClientData getById(@PathVariable Long id) {
        return clientDto.getById(id);
    }

    @GetMapping
    public Page<ClientData> getAll(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return clientDto.getAll(pageable);
    }

    @GetMapping("/filter")
    public List<ClientData> filter(@RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        return clientDto.filter(id, name);
    }

    @GetMapping("/search")
    public Page<ClientData> search(@RequestParam String name,
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return clientDto.searchByName(name, pageable);
    }
}
