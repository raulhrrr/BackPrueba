package com.prueba.api.dtos;

import lombok.Data;

@Data
public class BasicResponse {

    private Integer statusCode;
    private String message;

}
