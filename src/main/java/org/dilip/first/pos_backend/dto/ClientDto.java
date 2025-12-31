package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.ClientApi;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.model.data.ClientData;
import org.dilip.first.pos_backend.model.form.ClientForm;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientDto {

    @Autowired
    private ClientApi clientApi;

    public ClientData createClient(ClientForm form) {

        String name = form.getName();
        String email = StringUtil.normalizeToLowerCase(form.getEmail());
        String phone = form.getPhone();

        ClientEntity entity = clientApi.createClient(name, email, phone);
        return convertEntityToClientData(entity);
    }

    public List<ClientData> getAll() {
        return clientApi.getAll()
                .stream()
                .map(this::convertEntityToClientData)
                .toList();
    }

    private ClientData convertEntityToClientData(ClientEntity entity) {
        ClientData data = new ClientData();
        data.setId(entity.getId());
        data.setName(entity.getName());
        data.setEmail(entity.getEmail());
        data.setPhone(entity.getPhone());
        return data;
    }
}


