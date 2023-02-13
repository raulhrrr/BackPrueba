package com.prueba.api.repositories;

import com.prueba.api.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c " +
            "from Client c " +
            "where c.person.names like %?1% " +
            "or c.person.address like %?1% " +
            "or c.person.phone like %?1%")
    Set<Client> getAllClientsByFilter(String filter);

}
