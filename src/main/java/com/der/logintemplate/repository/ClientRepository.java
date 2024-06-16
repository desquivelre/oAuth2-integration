package com.der.logintemplate.repository;

import com.der.logintemplate.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    public Client findByUsername(String username);
}
