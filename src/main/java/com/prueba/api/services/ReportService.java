package com.prueba.api.services;

import com.prueba.api.dtos.ReportResponseDTO;
import com.prueba.api.repositories.ClientRepository;
import com.prueba.api.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Qualifier("reports")
public class ReportService implements ICrudService<ReportResponseDTO, ReportResponseDTO> {

    private final TransactionRepository transactionRepository;

    private final ClientRepository clientRepository;

    @Override
    public Set<ReportResponseDTO> getByDatesAndId(Date startDate, Date endDate, Integer clientId) {

        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException(String.format("No existe el cliente con id %d", clientId));
        }

        return transactionRepository.getAllTrasactionsByClientId(startDate, endDate, clientId);
    }

}
