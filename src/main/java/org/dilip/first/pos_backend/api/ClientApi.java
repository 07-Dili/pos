package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.dao.ClientDao;
import org.dilip.first.pos_backend.entity.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientApi {

    @Autowired
    private  ClientDao clientDao;

    public ClientEntity createClient(String name, String email, String phone) {
        if (clientDao.findByEmail(email) != null) {
            throw new RuntimeException("Client already exists");
        }
        if (phone == null || !phone.matches("^(\\d{10}|\\+[1-9]\\d{1,14})$")) {
            throw new RuntimeException("Invalid phone number");
        }
//        only 10 digits is enough
        ClientEntity entity = new ClientEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setPhone(phone);

        return clientDao.save(entity);
    }

    public List<ClientEntity> getAll() {
        return clientDao.findAll();
    }
}

