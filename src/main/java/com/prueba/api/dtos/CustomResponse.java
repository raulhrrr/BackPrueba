package com.prueba.api.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class CustomResponse<T> extends BasicResponse {

    private Set<T> data;

}
