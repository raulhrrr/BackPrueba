package com.prueba.api.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {

    private Integer statusCode;
    private String message;

}
