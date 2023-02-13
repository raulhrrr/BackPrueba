package com.prueba.api.repositories;

import com.prueba.api.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select p from Person p where p.id <> ?1 and p.identification = ?2")
    Optional<Person> getPersonByIdentificationAndId(Integer id, Integer identification);

    Boolean existsPersonByIdentification(Integer identification);

}
