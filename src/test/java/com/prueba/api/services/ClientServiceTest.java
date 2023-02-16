package com.prueba.api.services;

import com.prueba.api.dtos.ClientDTO;
import com.prueba.api.dtos.PersonDTO;
import com.prueba.api.entities.Client;
import com.prueba.api.entities.Person;
import com.prueba.api.exceptions.BadObjectException;
import com.prueba.api.exceptions.ConstraintViolationException;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.PersonRepository;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        clientService = new ClientService(
                clientRepository,
                personRepository,
                modelMapper
        );

    }

    @Test
    void getAll() {

        Person personOne = new Person();
        personOne.setId(1);
        personOne.setPhone("555123456");

        Client clientOne = new Client();
        clientOne.setId(1);
        clientOne.setPassword("123");
        clientOne.setStatus(true);
        clientOne.setPerson(personOne);

        Client clientTwo = new Client();
        clientTwo.setId(2);
        clientTwo.setPassword("321");
        clientTwo.setStatus(false);

        Set<Client> clients = new HashSet<>();
        clients.add(clientOne);
        clients.add(clientTwo);

        Mockito.when(clientRepository.getAllClientsByFilter(Mockito.anyString())).thenReturn(clients);

        Set<ClientDTO> clientsDtos = clientService.getAll("filtro");

        assertAll(
                () -> {
                    Optional<ClientDTO> first = clientsDtos.stream().filter(clientDTO -> clientDTO.getId() == 1).findFirst();
                    assertFalse(first.isPresent() && Objects.isNull(first.get().getPerson()));
                },
                () -> {
                    Optional<ClientDTO> first = clientsDtos.stream().filter(clientDTO -> clientDTO.getId() == 2).findFirst();
                    assertTrue(Objects.isNull(first.isPresent() ? first.get().getPerson() : false));
                }
        );

    }

    // Create method
    @Test
    void when_ClientIdAndPersonIdIsNotNull_then_ThrowBadObjectException() {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(1);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1);
        clientDTO.setPerson(personDTO);

        BadObjectException badObjectException = assertThrows(BadObjectException.class, () -> clientService.create(clientDTO));
        assertEquals("Este método no soporta la actualización de clientes", badObjectException.getMessage());

    }

    @Test
    void when_PersonExists_then_ThrowConstraintViolationException() {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setIdentification(123456789);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setPerson(personDTO);

        Mockito.when(personRepository.existsPersonByIdentification(Mockito.anyInt())).thenReturn(true);

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> clientService.create(clientDTO));
        assertEquals("Ya existe una persona con la identificación 123456789", constraintViolationException.getMessage());

    }

    @Test
    void when_ClientDtoIsCorrect_then_FinishProcess() {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setIdentification(123456789);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setPerson(personDTO);

        Mockito.when(personRepository.existsPersonByIdentification(Mockito.anyInt())).thenReturn(false);

        clientService.create(clientDTO);

    }

    // Update method
    @Test
    void when_ClientDoesntExists_then_ThrowEntityNotFoundException() {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(1);
        personDTO.setIdentification(123456789);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1);
        clientDTO.setPerson(personDTO);

        assertAll(
                () -> {
                    Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(false);
                    EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> clientService.update(clientDTO));
                    assertEquals("No se puede actualizar el cliente con id 1 ya que no existe", entityNotFoundException.getMessage());
                },
                () -> {
                    Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(true);
                    Mockito.when(personRepository.existsById(Mockito.anyInt())).thenReturn(false);
                    EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> clientService.update(clientDTO));
                    assertEquals("No se puede actualizar la persona con id 1 ya que no existe", entityNotFoundException.getMessage());
                },
                () -> {
                    Person personOne = new Person();
                    personOne.setId(1);
                    personOne.setIdentification(123456);

                    Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(true);
                    Mockito.when(personRepository.existsById(Mockito.anyInt())).thenReturn(true);
                    Mockito.when(personRepository.getPersonByIdentificationAndId(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.empty());
                    ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> clientService.update(clientDTO));
                    assertEquals("Ya existe una persona con la identificación 123456789", constraintViolationException.getMessage());
                }
        );


    }

    @Test
    void delete() {

        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(false);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> clientService.delete(1));
        assertEquals("No se encontró el cliente con el id 1", entityNotFoundException.getMessage());

    }
}