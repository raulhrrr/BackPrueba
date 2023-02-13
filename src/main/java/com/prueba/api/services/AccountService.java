package com.prueba.api.services;

import com.prueba.api.dtos.AccountDTO;
import com.prueba.api.entities.Account;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.repositories.AccountRepository;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("accounts")
public class AccountService implements IBasicCrudService<AccountDTO> {

    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    @Override
    public Set<AccountDTO> getAll(String filter) {
        Set<Account> accounts = accountRepository.getAllAccountsByFilter(filter);
        List<Integer> accountsIds = accounts.stream().map(Account::getId).collect(Collectors.toList());



        accounts.forEach(account -> {});
        //BigDecimal currentBalance = transactionRepository.getCurrentBalance(dto.getAccountId());

        return modelMapper.map(accounts, new TypeToken<Set<AccountDTO>>() {
        }.getType());
    }

    @Override
    public void create(AccountDTO accountDTO) {

        if (accountDTO.getId() != null) {
            throw new BadObjectException("Este método no soporta la actualización de cuentas");
        }

        if (!clientRepository.existsById(accountDTO.getClientId())) {
            throw new EntityNotFoundException(String.format("No existe el cliente con id %d para la cuenta %s", accountDTO.getClientId(), accountDTO.getAccountNumber()));
        }

        if (accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new ConstraintViolationException(String.format("Ya existe la cuenta %s", accountDTO.getAccountNumber()));
        }

        Account account = modelMapper.map(accountDTO, Account.class);
        accountRepository.save(account);
    }

    @Override
    public void update(AccountDTO dto) {

        Integer id = dto.getId();
        String accountNumber = dto.getAccountNumber();

        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("No se puede actualizar la cuenta con id %d ya que no existe", id));
        }

        if (!clientRepository.existsById(dto.getClientId())) {
            throw new EntityNotFoundException(String.format("No existe el cliente con id %d para la cuenta %s", dto.getClientId(), dto.getAccountNumber()));
        }

        Optional<Account> accountOptional = accountRepository.getAccountByIdentificationAndId(id, accountNumber);

        if (accountOptional.isPresent()) {
            throw new ConstraintViolationException(String.format("Ya existe otro cliente con el número de cuenta %s", accountNumber));
        }

        Account account = modelMapper.map(dto, Account.class);
        accountRepository.save(account);

    }

    @Override
    public void delete(Integer id) {

        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("No se encontró la cuenta el id %d", id));
        }

        accountRepository.deleteById(id);
    }

}
