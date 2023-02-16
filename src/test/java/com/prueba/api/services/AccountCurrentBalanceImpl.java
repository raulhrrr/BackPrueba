package com.prueba.api.services;

import com.prueba.api.projections.AccountCurrentBalance;

import java.math.BigDecimal;

public class AccountCurrentBalanceImpl implements AccountCurrentBalance {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public BigDecimal getBalance() {
        return BigDecimal.TEN;
    }

}
