package com.prueba.api.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prueba.api.utils.CustomTransactionTypeDeserializer;
import com.prueba.api.utils.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionResponseDTO {

    private Integer id;

    @JsonDeserialize(using = CustomTransactionTypeDeserializer.class)
    private TransactionType type;

    private BigDecimal balance;

    private BigDecimal value;

    private Boolean status;

    private LocalDateTime createdAt;

    private AccountResponseDTO account;

}
