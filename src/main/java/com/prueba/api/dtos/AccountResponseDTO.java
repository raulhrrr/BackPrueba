package com.prueba.api.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prueba.api.utils.AccountType;
import com.prueba.api.utils.CustomAccountTypeDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountResponseDTO {

    private Integer id;

    private String accountNumber;

    @JsonDeserialize(using = CustomAccountTypeDeserializer.class)
    private AccountType type;

    private BigDecimal currentBalance;

    private BigDecimal initialBalance;

    private Boolean status;

    private ClientDTO client;

}
