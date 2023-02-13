package com.prueba.api.repositories;

import com.prueba.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("select a from Account a where a.id <> ?1 and a.accountNumber = ?2")
    Optional<Account> getAccountByIdentificationAndId(Integer id, String accountNumber);

    @Query("select a from Account a " +
            "where a.accountNumber like %?1% " +
            "or a.client.person.names like %?1% " +
            "or cast(a.client.person.identification as string) like %?1%")
    Set<Account> getAllAccountsByFilter(String filter);

    Boolean existsByAccountNumber(String accountNumber);

}
