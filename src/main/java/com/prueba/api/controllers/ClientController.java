package com.prueba.api.controllers;

import com.prueba.api.dtos.BasicResponse;
import com.prueba.api.dtos.ClientDTO;
import com.prueba.api.dtos.CustomResponse;
import com.prueba.api.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/clientes")
public class ClientController {

    @Qualifier("clients")
    private final ICrudService<ClientDTO, ClientDTO> clientService;

    @GetMapping("/obtener")
    public ResponseEntity<CustomResponse<ClientDTO>> getAllClients(@RequestParam(value = "filtro", defaultValue = "") String filtro) {

        CustomResponse<ClientDTO> response = new CustomResponse<>();
        response.setStatusCode(200);
        response.setMessage("Todo bien");
        response.setData(clientService.getAll(filtro));

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<BasicResponse> createClient(@Valid @RequestBody ClientDTO clientDTO) {

        clientService.create(clientDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<BasicResponse> updateClient(@Valid @RequestBody ClientDTO clientDTO) {

        clientService.update(clientDTO);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<BasicResponse> deleteClient(@PathVariable Integer id) {

        clientService.delete(id);

        BasicResponse response = new BasicResponse();
        response.setStatusCode(200);
        response.setMessage("Todo bien");

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

}
