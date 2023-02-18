package com.prueba.api.services;

import com.prueba.api.dtos.TransactionDTO;
import com.prueba.api.dtos.TransactionResponseDTO;
import com.prueba.api.entities.Transaction;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.projections.IAccountCurrentBalance;
import com.prueba.api.repositories.AccountRepository;
import com.prueba.api.repositories.TransactionRepository;
import com.prueba.api.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("transactions")
public class TransactionService implements ICrudService<TransactionDTO, TransactionResponseDTO> {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Set<TransactionResponseDTO> getAllByDate(Date startDate, Date endDate, String filter) {
        Set<Transaction> transactions = transactionRepository.getAllTransactionsByFilter(startDate, endDate, filter);
        return modelMapper.map(transactions, new TypeToken<Set<TransactionResponseDTO>>() {
        }.getType());
    }

    @Override
    public void create(TransactionDTO dto) {

        if (!accountRepository.existsById(dto.getAccountId())) {
            throw new EntityNotFoundException(String.format("No existe la cuenta con id %d", dto.getAccountId()));
        }

        BigDecimal currentBalance = transactionRepository.getCurrentBalanceByAccountsIds(List.of(dto.getAccountId()))
                .stream().collect(Collectors.toMap(IAccountCurrentBalance::getId, IAccountCurrentBalance::getBalance)).get(dto.getAccountId());
        BigDecimal sum = currentBalance.add(dto.getValue());

        if (sum.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConstraintViolationException("No hay saldo suficiente para realizar la transacción");
        }

        validateValue(dto);

        Transaction transaction = modelMapper.map(dto, Transaction.class);
        transaction.setBalance(sum);
        transactionRepository.save(transaction);

    }

    @Override
    public void update(TransactionDTO dto) {

        if (!transactionRepository.existsById(dto.getId())) {
            throw new EntityNotFoundException(String.format("No se puede actualizar la transacción con id %d ya que no existe", dto.getId()));
        }

        if (!accountRepository.existsById(dto.getAccountId())) {
            throw new EntityNotFoundException(String.format("No se puede actualizar la cuenta con id %d ya que no existe", dto.getAccountId()));
        }

        Optional<Transaction> transaction = transactionRepository.findById(dto.getId());
        BigDecimal currentBalance = transactionRepository.getCurrentBalanceByAccountsIds(List.of(dto.getAccountId()))
                .stream().collect(Collectors.toMap(IAccountCurrentBalance::getId, IAccountCurrentBalance::getBalance)).get(dto.getAccountId());
        BigDecimal newValue = currentBalance.add(transaction.map(transac -> transac.getValue().multiply(new BigDecimal("-1"))).orElse(BigDecimal.ZERO))
                .add(dto.getValue());

        if (newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConstraintViolationException("No hay saldo suficiente para realizar la transacción");
        }

        validateValue(dto);

        Transaction newTransaction = modelMapper.map(dto, Transaction.class);
        newTransaction.setBalance(newValue);
        newTransaction.setCreatedAt(transaction.isPresent() ? transaction.get().getCreatedAt() : LocalDateTime.now());
        transactionRepository.save(newTransaction);

    }

    @Override
    public void delete(Integer id) {

        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("No se encontró la transacción con id %d", id));
        }

        transactionRepository.deleteById(id);

    }

    public void validateValue(TransactionDTO dto) {

        boolean isValid = dto.getType().equals(TransactionType.DEPOSITO)
                ? dto.getValue().compareTo(BigDecimal.ZERO) > 0
                : dto.getValue().compareTo(BigDecimal.ZERO) < 0;

        if (!isValid) {
            String message = dto.getType().equals(TransactionType.DEPOSITO)
                    ? "El valor para un depósito debe ser positivo"
                    : "El valor para un retiro debe ser negativo";
            throw new BadObjectException(message);
        }
    }

}
