package com.prueba.api.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {

    DEPOSITO("Deposito"),
    RETIRO("Retiro");

    private final String name;

}
