package com.prueba.api.services;

import com.prueba.api.dtos.AccountDTO;
import com.prueba.api.dtos.AccountResponseDTO;
import com.prueba.api.entities.Account;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.repositories.AccountRepository;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.TransactionRepository;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        accountService = new AccountService(
                transactionRepository,
                clientRepository,
                accountRepository,
                modelMapper
        );

    }

    @Test
    void getAll() {

        Account accountOne = new Account();
        accountOne.setId(1);

        Account accountTwo = new Account();
        accountTwo.setId(1);

        Mockito.when(accountRepository.getAllAccountsByFilter(Mockito.anyString())).thenReturn(Set.of(accountOne, accountTwo));
        Mockito.when(transactionRepository.getCurrentBalanceByAccountsIds(Mockito.anyList())).thenReturn(Set.of(new AccountCurrentBalanceImpl()));

        Set<AccountResponseDTO> accounts = accountService.getAll("filtro");

        assertAll(
                () -> assertEquals(1, accounts.size()),
                () -> {
                    Optional<AccountResponseDTO> first = accounts.stream().filter(account -> account.getId() == 1).findFirst();
                    assertEquals(new BigDecimal("10"), first.isPresent() ? first.get().getCurrentBalance() : BigDecimal.ZERO);
                }
        );

    }

    @Test
    void when_AccountIdIsNotNull_then_ThrowBadObjectException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);

        BadObjectException badObjectException = assertThrows(BadObjectException.class, () -> accountService.create(accountDTO));
        assertEquals("Este método no soporta la actualización de cuentas", badObjectException.getMessage());

    }

    @Test
    void when_ClientDoesntExists_then_ThrowEntityNotFoundException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setClientId(1);
        accountDTO.setAccountNumber("123");

        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(false);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> accountService.create(accountDTO));
        assertEquals("No existe el cliente con id 1 para la cuenta 123", entityNotFoundException.getMessage());

    }

    @Test
    void when_AccountNumberAlreadyExists_then_ThrowConstraintViolationException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setClientId(1);
        accountDTO.setAccountNumber("123");

        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(accountRepository.existsByAccountNumber(Mockito.anyString())).thenReturn(true);

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> accountService.create(accountDTO));
        assertEquals("Ya existe la cuenta 123", constraintViolationException.getMessage());

    }

    // update
    @Test
    void when_AccountDoesNotExists_then_ThrowEntityNotFoundException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(false);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> accountService.update(accountDTO));
        assertEquals("No se puede actualizar la cuenta con id 1 ya que no existe", entityNotFoundException.getMessage());

    }

    @Test
    void when_ClientDoesNotExists_then_ThrowEntityNotFoundException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setClientId(1);
        accountDTO.setAccountNumber("321");

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(false);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> accountService.update(accountDTO));
        assertEquals("No existe el cliente con id 1 para la cuenta 321", entityNotFoundException.getMessage());

    }

    @Test
    void when_OtherClientHasTheSameAccountNumber_then_ThrowConstraintViolationException() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setClientId(1);
        accountDTO.setAccountNumber("321");

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(accountRepository.getAccountByIdentificationAndId(Mockito.anyInt(), Mockito.anyString())).thenReturn(Optional.empty());

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> accountService.update(accountDTO));
        assertEquals("Ya existe otro cliente con el número de cuenta 321", constraintViolationException.getMessage());

    }

    @Test
    void delete() {

        Mockito.when(accountRepository.existsById(Mockito.anyInt())).thenReturn(false);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> accountService.delete(1));
        assertEquals("No se encontró la cuenta el id 1", entityNotFoundException.getMessage());

    }


}