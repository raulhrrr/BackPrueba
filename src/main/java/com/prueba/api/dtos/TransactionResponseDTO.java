package com.prueba.api.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prueba.api.utils.CustomTransactionTypeDeserializer;
import com.prueba.api.utils.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransactionResponseDTO {

    private Integer id;

    @JsonDeserialize(using = CustomTransactionTypeDeserializer.class)
    private TransactionType type;

    private BigDecimal value;

    private Boolean status;

    private AccountResponseDTO account;

}
