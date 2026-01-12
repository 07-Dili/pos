package org.dilip.first.pos_backend.dto;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.model.clients.ClientData;
import org.dilip.first.pos_backend.model.clients.ClientForm;
import org.dilip.first.pos_backend.model.clients.ClientSearchForm;
import org.dilip.first.pos_backend.model.clients.ClientUpdateForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientDto {

    @Autowired
    private ClientApi clientApi;

    public ClientData update(Long id, @Valid ClientUpdateForm form) {

        String name = StringUtil.normalizeToLowerCase(form.getName());
        String email = StringUtil.normalizeToLowerCase(form.getEmail());
        String phone = form.getPhone();

        ClientEntity entity = clientApi.update(id, name, email, phone);
        return EntityToData.convertClientEntityToData(entity);
    }

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

    public List<ClientData> getAll(int page, int size) {

        List<ClientEntity> entities = clientApi.getAll(page, size);
        return entities.stream()
                .map(EntityToData::convertClientEntityToData)
                .toList();
    }

    public List<ClientData> search(ClientSearchForm form) {

        Long id = form.getId();

        String name = StringUtil.normalizeToLowerCase(form.getName());
        String email = StringUtil.normalizeToLowerCase(form.getEmail());

        if (name != null && name.isBlank()) name = null;
        if (email != null && email.isBlank()) email = null;

        int page = form.getPage();
        int size = form.getSize();

        List<ClientEntity> entities = clientApi.search(id, name, email, page, size);

        return entities.stream()
                .map(EntityToData::convertClientEntityToData)
                .toList();
    }

}
