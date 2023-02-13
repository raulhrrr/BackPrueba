package com.prueba.api.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {

    private Integer id;

    private String names;

    @NotNull(message = "El género no puede ser nulo")
    @NotBlank(message = "El género no puede estar en blanco")
    private String gender;

    private Integer age;

    private Integer identification;

    private String address;

    @NotNull(message = "El número de teléfono no puede ser nulo")
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Size(min = 8, message = "El número de teléfono debe tener mínimo 8 dígitos")
    private String phone;

}
