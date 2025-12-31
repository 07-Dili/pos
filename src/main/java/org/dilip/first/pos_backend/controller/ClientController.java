package org.dilip.first.pos_backend.controller;
import jakarta.validation.Valid;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.model.data.ClientData;
import org.dilip.first.pos_backend.model.form.ClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.dilip.first.pos_backend.dto.ClientDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @PostMapping
    public ClientData createClient(@Valid @RequestBody ClientForm form) {
        return clientDto.createClient(form);

    }

    @GetMapping
    public List<ClientData> getAll() {
        return clientDto.getAll();
    }
    @GetMapping("/{id}")
    public List<ClientData> getById() {
        return clientDto.getAll();
    }
}
