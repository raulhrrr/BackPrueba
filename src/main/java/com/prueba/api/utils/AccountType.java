package com.prueba.api.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {

    AHORROS("Ahorros"),
    CORRIENTE("Corriente");

    private final String name;

}
