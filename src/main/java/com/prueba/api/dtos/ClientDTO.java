package com.prueba.api.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ClientDTO {

    private Integer id;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    private String password;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean status;

    @NotNull(message = "La persona no puede ser nula")
    private PersonDTO person;

}
