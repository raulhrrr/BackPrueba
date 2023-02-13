package com.prueba.api.dtos;

import com.prueba.api.utils.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {

    private Boolean transactionState;
    private BigDecimal transactionValue;
    private BigDecimal transactionResidue;
    private LocalDateTime createdAt;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal accountInitialBalance;
    private String clientName;

}
