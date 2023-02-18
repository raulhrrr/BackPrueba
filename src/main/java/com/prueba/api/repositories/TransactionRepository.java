package com.prueba.api.repositories;

import com.prueba.api.dtos.ReportResponseDTO;
import com.prueba.api.entities.Transaction;
import com.prueba.api.projections.IAccountCurrentBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(nativeQuery = true, value = "select a.id as id, a.initial_balance + (" +
            "select ifnull(sum(t.value), 0) from transaction t where t.id_account = a.id) as balance " +
            "from account a where a.status = 1 and a.id in (?1)")
    Set<IAccountCurrentBalance> getCurrentBalanceByAccountsIds(List<Integer> accountId);

    @Query("select t " +
            "from Transaction t " +
            "where cast(t.createdAt as date)  between ?1 and ?2 " +
            "and (t.account.client.person.names like %?3% " +
            "or cast(t.account.client.person.identification as string) like %?3% " +
            "or t.account.client.person.phone like %?3%)")
    Set<Transaction> getAllTransactionsByFilter(Date fromDate, Date toDate, String filter);

    @Query("select new com.prueba.api.dtos.ReportResponseDTO(" +
            "t.status," +
            "t.value," +
            "t.balance," +
            "t.createdAt, " +
            "t.account.accountNumber," +
            "t.account.type," +
            "t.account.initialBalance," +
            "t.account.client.person.names " +
            ") from Transaction t where cast(t.createdAt as date) between ?1 and ?2 and t.account.client.id = ?3")
    Set<ReportResponseDTO> getAllTrasactionsByClientId(Date fromDate, Date toDate, Integer clientId);

}
