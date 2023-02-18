package com.prueba.api.controllers;

import com.prueba.api.dtos.AccountDTO;
import com.prueba.api.dtos.AccountResponseDTO;
import com.prueba.api.dtos.BasicResponse;
import com.prueba.api.dtos.CustomResponse;
import com.prueba.api.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuentas")
public class AccountsController {

    @Qualifier("accounts")
    private final ICrudService<AccountDTO, AccountResponseDTO> accountService;

    @GetMapping("/obtener")
    public ResponseEntity<CustomResponse<AccountResponseDTO>> getAllClients(@RequestParam(value = "filtro", defaultValue = "") String filtro) {

        CustomResponse<AccountResponseDTO> response = new CustomResponse<>();
        response.setStatusCode(200);
        response.setMessage("Todo bien");
        response.setData(accountService.getAll(filtro));

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<BasicResponse> createAccount(@Valid @RequestBody AccountDTO accountDTO) {

        accountService.create(accountDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);

    }

    @PutMapping("/actualizar")
    public ResponseEntity<BasicResponse> updateAccount(@Valid @RequestBody AccountDTO accountDTO) {

        accountService.update(accountDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<BasicResponse> deleteAccount(@PathVariable Integer id) {

        accountService.delete(id);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

}
