package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.ClientDto;
import org.dilip.first.pos_backend.model.data.ClientData;
import org.dilip.first.pos_backend.model.form.ClientForm;
import org.dilip.first.pos_backend.model.form.ClientSearchForm;
import org.dilip.first.pos_backend.model.form.ClientUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @PutMapping("/{id}")
    public ClientData update( @PathVariable Long id, @Valid @RequestBody ClientUpdateForm form) {
        return clientDto.update(id, form);
    }

    @PostMapping
    public ClientData create(@Valid @RequestBody ClientForm form) {
        return clientDto.create(form);
    }

    @GetMapping("/{id}")
    public ClientData getById(@PathVariable Long id) {
        return clientDto.getById(id);
    }

    @GetMapping
    public List<ClientData> getAll( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return clientDto.getAll(page, size);
    }

    @GetMapping("/search")
    public List<ClientData> search(ClientSearchForm form) {
        return clientDto.search(form);
    }
}
