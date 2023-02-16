package com.prueba.api.services;

import com.prueba.api.dtos.TransactionDTO;
import com.prueba.api.dtos.TransactionResponseDTO;
import com.prueba.api.entities.Transaction;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.repositories.AccountRepository;
import com.prueba.api.repositories.TransactionRepository;
import com.prueba.api.utils.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        transactionService = new TransactionService(
                accountRepository,
                transactionRepository,
                modelMapper
        );

    }

    @Test
    void getAll() {

        Transaction transactionOne = new Transaction();
        transactionOne.setId(1);

        Transaction transactionTwo = new Transaction();
        transactionTwo.setId(2);

        Transaction transactionThree = new Transaction();
        transactionThree.setId(3);

        Mockito.when(transactionRepository.getAllTransactionsByFilter(Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyString())).thenReturn(Set.of(transactionOne, transactionTwo, transactionThree));

        Set<TransactionResponseDTO> transactions = transactionService.getAllByDate(Date.valueOf("2023-02-16"), Date.valueOf("2023-02-16"), "filtro");

        assertEquals(3, transactions.size());

    }

    // create
    @Test
    void when_AccountDoesNotExists_then_ThrowEntityNotFoundException() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(5);

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(false);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> transactionService.create(transactionDTO));
        assertEquals("No existe la cuenta con id 5", entityNotFoundException.getMessage());

    }

    @Test
    void when_CurrentBalanceIsLessThanZero_then_ThrowConstraintViolationException() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1);
        transactionDTO.setType(TransactionType.RETIRO);
        transactionDTO.setValue(new BigDecimal("-15"));

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(transactionRepository.getCurrentBalanceByAccountsIds(Mockito.anyList())).thenReturn(Set.of(new AccountCurrentBalanceImpl()));

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> transactionService.create(transactionDTO));
        assertEquals("No hay saldo suficiente para realizar la transacción", constraintViolationException.getMessage());

    }

    @Test
    void when_CurrentBalanceIsGreaterThanZero_then_Continue() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1);
        transactionDTO.setType(TransactionType.DEPOSITO);
        transactionDTO.setValue(new BigDecimal("15"));

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(transactionRepository.getCurrentBalanceByAccountsIds(Mockito.anyList())).thenReturn(Set.of(new AccountCurrentBalanceImpl()));
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(new Transaction());

        transactionService.create(transactionDTO);

    }

    // update
    @Test
    void when_TransactionDoesNotExists_then_ThrowEntityNotFoundException() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(8);

        Mockito.when(transactionRepository.existsById(Mockito.anyInt())).thenReturn(false);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> transactionService.update(transactionDTO));
        assertEquals("No se puede actualizar la transacción con id 8 ya que no existe", entityNotFoundException.getMessage());

    }

    @Test
    void when_CurrentBalanceUpdatedIsLessThanZero_then_ThrowConstraintViolationException() {

        Transaction oldTransaction = new Transaction();
        oldTransaction.setValue(new BigDecimal("25"));
        oldTransaction.setCreatedAt(LocalDateTime.now());


        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(8);
        transactionDTO.setValue(new BigDecimal("-50"));
        transactionDTO.setAccountId(1);

        Mockito.when(transactionRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(transactionRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(oldTransaction));
        Mockito.when(transactionRepository.getCurrentBalanceByAccountsIds(Mockito.anyList())).thenReturn(Set.of(new AccountCurrentBalanceImpl()));

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> transactionService.update(transactionDTO));
        assertEquals("No hay saldo suficiente para realizar la transacción", constraintViolationException.getMessage());

    }

    @Test
    void when_AllIsCorrect_then_FinishProcess() {

        Transaction oldTransaction = new Transaction();
        oldTransaction.setValue(new BigDecimal("25"));
        oldTransaction.setCreatedAt(LocalDateTime.now());


        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(8);
        transactionDTO.setType(TransactionType.DEPOSITO);
        transactionDTO.setValue(new BigDecimal("50"));
        transactionDTO.setAccountId(1);

        Mockito.when(transactionRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(transactionRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(oldTransaction));
        Mockito.when(transactionRepository.getCurrentBalanceByAccountsIds(Mockito.anyList())).thenReturn(Set.of(new AccountCurrentBalanceImpl()));
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(new Transaction());

        transactionService.update(transactionDTO);

    }

    @Test
    void delete() {

        Mockito.when(transactionRepository.existsById(Mockito.anyInt())).thenReturn(false);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> transactionService.delete(1));
        assertEquals("No se encontró la transacción con id 1", entityNotFoundException.getMessage());

    }

    @Test
    void validateValue() {

        TransactionDTO dto = new TransactionDTO();

        assertAll(
                () -> {
                    dto.setType(TransactionType.DEPOSITO);
                    dto.setValue(new BigDecimal("-100"));
                    BadObjectException badObjectException = assertThrows(BadObjectException.class, () -> transactionService.validateValue(dto));
                    assertEquals("El valor para un depósito debe ser positivo", badObjectException.getMessage());
                },
                () -> {
                    dto.setType(TransactionType.RETIRO);
                    dto.setValue(new BigDecimal("100"));
                    BadObjectException badObjectException = assertThrows(BadObjectException.class, () -> transactionService.validateValue(dto));
                    assertEquals("El valor para un retiro debe ser negativo", badObjectException.getMessage());
                }
        );


    }

}