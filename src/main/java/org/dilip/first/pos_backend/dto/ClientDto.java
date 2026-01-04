package org.dilip.first.pos_backend.dto;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.model.data.ClientData;
import org.dilip.first.pos_backend.model.form.ClientForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientDto {

    @Autowired
    private ClientApi clientApi;

    public ClientData create(@Valid ClientForm form) {

        String name = StringUtil.normalizeToLowerCase(form.getName());
        String email = StringUtil.normalizeToLowerCase(form.getEmail());
        String phone = form.getPhone();

        ClientEntity entity = clientApi.create(name, email, phone);
        return EntityToData.convertClientEntityToData(entity);
    }

    public ClientData getById(Long id) {
        ClientEntity entity = clientApi.getById(id);
        return EntityToData.convertClientEntityToData(entity);
    }

    public List<ClientData> filter(Long id, String name) {
        return clientApi.filter(id, name).stream().map(EntityToData::convertClientEntityToData).toList();
    }

    public Page<ClientData> getAll(Pageable pageable) {
        return clientApi.getAll(pageable).map(EntityToData::convertClientEntityToData);
    }

    public Page<ClientData> searchByName(String name, Pageable pageable) {
        String normalizedName = StringUtil.normalizeToLowerCase(name);
        return clientApi.searchByName(normalizedName, pageable).map(EntityToData::convertClientEntityToData);
    }
}
