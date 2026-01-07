package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientApi {

    @Autowired
    private ClientDao clientDao;

    public ClientEntity update(Long id, String name, String email, String phone) {

        ClientEntity existing = getById(id);

        ClientEntity duplicate = clientDao.findByName(name);
        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Client already exists " + name);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setPhone(phone);

        return clientDao.save(existing);
    }

    public ClientEntity create(String name, String email, String phone) {

        if (clientDao.findByName(name) != null) {
            throw new ApiException(HttpStatus.CONFLICT, "Client already exists " + name);
        }

        ClientEntity entity = new ClientEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setPhone(phone);

        return clientDao.save(entity);
    }

    public ClientEntity getById(Long id) {
        return clientDao.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Client not found " + id));
    }

    public List<ClientEntity> getAll(int page, int size) {

        int offset = page * size;
        return clientDao.findAllWithPagination(size, offset);
    }

    public List<ClientEntity> search(Long id, String name, String email, int page, int size) {

        int offset = page * size;
        return clientDao.search(id, name, email, size, offset);
    }
}
