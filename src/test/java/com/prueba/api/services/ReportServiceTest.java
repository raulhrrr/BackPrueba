package com.prueba.api.services;

import com.prueba.api.dtos.ReportResponseDTO;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.TransactionRepository;
import com.prueba.api.utils.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void when_ClientDoesntExists_then_ThrowEntityNotFoundException() {

        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(false);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> reportService.getByDatesAndId(Date.valueOf("2023-02-17"), Date.valueOf("2023-02-17"), 1));
        assertEquals("No existe el cliente con id 1", entityNotFoundException.getMessage());

    }

    @Test
    void when_AllIsCorrect_then_FinishMethod() {

        ReportResponseDTO reportResponseDTO1 = new ReportResponseDTO();
        reportResponseDTO1.setAccountNumber("123");
        reportResponseDTO1.setAccountType(AccountType.AHORROS);

        ReportResponseDTO reportResponseDTO2 = new ReportResponseDTO();
        reportResponseDTO2.setAccountNumber("321");
        reportResponseDTO2.setAccountType(AccountType.CORRIENTE);

        Mockito.when(clientRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(transactionRepository.getAllTrasactionsByClientId(Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyInt())).thenReturn(Set.of(reportResponseDTO1, reportResponseDTO2));

        Set<ReportResponseDTO> reports = reportService.getByDatesAndId(Date.valueOf("2023-02-17"), Date.valueOf("2023-02-17"), 1);
        assertEquals(2, reports.size());

    }
}