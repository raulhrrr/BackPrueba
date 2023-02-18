package com.prueba.api.services;

import com.prueba.api.dtos.ClientDTO;
import com.prueba.api.entities.Client;
import com.prueba.api.entities.Person;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Qualifier("clients")
public class ClientService implements ICrudService<ClientDTO, ClientDTO> {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    public Set<ClientDTO> getAll(String filter) {
        Set<Client> clients = clientRepository.getAllClientsByFilter(filter);
        return modelMapper.map(clients, new TypeToken<Set<ClientDTO>>() {
        }.getType());
    }

    @Override
    public void create(ClientDTO clientDTO) {

        Integer identification = clientDTO.getPerson().getIdentification();

        if (clientDTO.getId() != null || clientDTO.getPerson().getId() != null) {
            throw new BadObjectException("Este método no soporta la actualización de clientes");
        }

        if (personRepository.existsPersonByIdentification(identification)) {
            throw new ConstraintViolationException(String.format("Ya existe una persona con la identificación %d", identification));
        }

        Client client = modelMapper.map(clientDTO, Client.class);
        clientRepository.save(client);

    }

    @Override
    public void update(ClientDTO clientDTO) {

        Integer id = clientDTO.getPerson().getId();
        Integer identification = clientDTO.getPerson().getIdentification();

        if (!clientRepository.existsById(clientDTO.getId())) {
            throw new EntityNotFoundException(String.format("No se puede actualizar el cliente con id %d ya que no existe", clientDTO.getId()));
        }

        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("No se puede actualizar la persona con id %d ya que no existe", id));
        }

        Optional<Person> person = personRepository.getPersonByIdentificationAndId(id, identification);

        if (person.isEmpty()) {
            throw new ConstraintViolationException(String.format("Ya existe una persona con la identificación %d", identification));
        }

        Client client = modelMapper.map(clientDTO, Client.class);
        clientRepository.save(client);

    }

    @Override
    public void delete(Integer id) {

        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("No se encontró el cliente con el id %d", id));
        }

        clientRepository.deleteById(id);
    }

}
