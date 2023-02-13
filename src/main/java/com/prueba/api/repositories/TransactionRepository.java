package com.prueba.api.repositories;

import com.prueba.api.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(nativeQuery = true, value = "select a.initial_balance + (" +
            "select ifnull(sum(t.value), 0) from transaction t where t.id_account = a.id) " +
            "from account a where a.status = 1 and a.id in (?1)")
    List<BigDecimal> getCurrentBalanceByAccountsIds(List<Integer> accountId);

    @Query("select t " +
            "from Transaction t " +
            "where cast(t.createdAt as date)  between ?1 and ?2 " +
            "and (t.account.client.person.names like %?3% " +
            "or cast(t.account.client.person.identification as string) like %?3% " +
            "or t.account.client.person.phone like %?3%)")
    Set<Transaction> getAllTransactionsByFilter(Date fromDate, Date toDate, String filter);

}
