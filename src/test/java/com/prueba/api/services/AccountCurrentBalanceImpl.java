package com.prueba.api.services;

import com.prueba.api.projections.IAccountCurrentBalance;

import java.math.BigDecimal;

public class AccountCurrentBalanceImpl implements IAccountCurrentBalance {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public BigDecimal getBalance() {
        return BigDecimal.TEN;
    }

}
