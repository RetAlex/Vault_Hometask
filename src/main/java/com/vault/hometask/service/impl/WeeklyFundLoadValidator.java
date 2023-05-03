package com.vault.hometask.service.impl;

import com.vault.hometask.repository.LoadFundsTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.util.dto.LoadFundsRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WeeklyFundLoadValidator implements FundsLoadValidator {
    private final static double WEEKLY_AMOUNT_LIMIT = 20000.0;
    private final LoadFundsTransactionRepository loadFundsTransactionRepository;

    public WeeklyFundLoadValidator(LoadFundsTransactionRepository loadFundsTransactionRepository) {
        this.loadFundsTransactionRepository = loadFundsTransactionRepository;
    }

    @Override
    public boolean validate(LoadFundsRequest payload) {
        // We skip transaction if the amount is bigger than limit before fetching data from DB for optimization purposes
        if (payload.getLoadAmount() > WEEKLY_AMOUNT_LIMIT) return false;

        return amountLimitNotBreached(payload.getCustomerId(), payload.getLoadAmount(), payload.getTime());
    }

    private boolean amountLimitNotBreached(long customerId, double amount, Date date){
        return loadFundsTransactionRepository.findWeeklyTransactionSumById(customerId, date).orElse(0.0)+amount < WEEKLY_AMOUNT_LIMIT;
    }
}
