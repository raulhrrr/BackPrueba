package com.prueba.api.controllers;

import com.prueba.api.dtos.CustomResponse;
import com.prueba.api.dtos.ReportResponseDTO;
import com.prueba.api.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reportes")
public class ReportController {

    @Qualifier("reports")
    private final ICrudService<ReportResponseDTO, ReportResponseDTO> reportsService;

    @GetMapping("/obtener")
    public ResponseEntity<CustomResponse<ReportResponseDTO>> getReport(
            @RequestParam("fechaInicio") Date fechaInicio,
            @RequestParam("fechaFin") Date fechaFin,
            @RequestParam("clientId") Integer clientId
    ) {

        CustomResponse<ReportResponseDTO> response = new CustomResponse<>();
        response.setStatusCode(200);
        response.setMessage("Todo bien");
        response.setData(reportsService.getByDatesAndId(fechaInicio, fechaFin, clientId));

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }

}
