package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientApi {

    @Autowired
    private ClientDao clientDao;

    public ClientEntity create(String name, String email, String phone) {

        if (clientDao.findByName(name) != null) {
            throw new ApiException(409, "Client already exists "+name);
        }

        ClientEntity entity = new ClientEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setPhone(phone);

        return clientDao.save(entity);
    }

    public ClientEntity getById(Long id) {
        return clientDao.findById(id)
                .orElseThrow(() -> new ApiException(404, "Client not found "+id));
    }

    public List<ClientEntity> filter(Long id, String name) {
        return clientDao.filter(id, name);
    }

    public Page<ClientEntity> getAll(Pageable pageable) {
        return clientDao.findAll(pageable);
    }

    public Page<ClientEntity> searchByName(String name, Pageable pageable) {
        return clientDao.searchByName(name, pageable);
    }

}
