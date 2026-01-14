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

    public ClientEntity create(String name, String email, String phone) {

        if (clientDao.findByName(name) != null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Client already exists " + name);
        }

        ClientEntity client = clientDao.findByEmail(email);
        if(client != null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client already exists : " + email);
        }

        ClientEntity entity = new ClientEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setPhone(phone);
        return clientDao.save(entity);
    }

    public ClientEntity update(Long id, String name, String email, String phone) {

        if(phone.length()!=10) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Phone number must be exactly 10 digits");
        }

        ClientEntity existing = getById(id);
        ClientEntity duplicate = clientDao.findByName(name);

        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client already exists " + name);
        }

        ClientEntity client = clientDao.findByEmail(email);
        if(client != null && !client.getId().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client already exists : " + email);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setPhone(phone);
        return clientDao.save(existing);
    }

    public ClientEntity getById(Long id) {

        ClientEntity client = clientDao.findById(ClientEntity.class, id);
        if (client == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Client not found " + id);
        }
        return client;
    }

    public List<ClientEntity> getAll(int page, int size) {
        return clientDao.findAll(ClientEntity.class,page, size);
    }

    public List<ClientEntity> search(Long id, String name, String email, int page, int size) {
        return clientDao.search(id, name, email, page, size);
    }
}
