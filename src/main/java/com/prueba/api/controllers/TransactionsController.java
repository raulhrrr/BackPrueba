package com.prueba.api.controllers;

import com.prueba.api.dtos.BasicResponse;
import com.prueba.api.dtos.CustomResponse;
import com.prueba.api.dtos.TransactionDTO;
import com.prueba.api.dtos.TransactionResponseDTO;
import com.prueba.api.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movimientos")
public class TransactionsController {

    @Qualifier("transactions")
    private final ICrudService<TransactionDTO, TransactionResponseDTO> transactionsService;

    @GetMapping("/obtener")
    public ResponseEntity<CustomResponse<TransactionResponseDTO>> getAllTransactions(
            @RequestParam("fechaInicio") Date fechaInicio,
            @RequestParam("fechaFin") Date fechaFin,
            @RequestParam(value = "filtro", defaultValue = "") String filtro
    ) {

        CustomResponse<TransactionResponseDTO> response = new CustomResponse<>();
        response.setStatusCode(200);
        response.setMessage("Todo bien");
        response.setData(transactionsService.getAllByDate(fechaInicio, fechaFin, filtro));

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<BasicResponse> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {

        transactionsService.create(transactionDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);

    }

    @PutMapping("/actualizar")
    public ResponseEntity<BasicResponse> updateAccount(@Valid @RequestBody TransactionDTO accountDTO) {

        transactionsService.update(accountDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<BasicResponse> deleteAccount(@PathVariable Integer id) {

        transactionsService.delete(id);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

}
