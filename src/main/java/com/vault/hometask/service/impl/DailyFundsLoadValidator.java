package com.vault.hometask.service.impl;

import com.vault.hometask.entity.LoadFundsTransactionEntity;
import com.vault.hometask.repository.LoadFundsTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.util.dto.LoadFundsRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class DailyFundsLoadValidator implements FundsLoadValidator {
    private final static double DAILY_AMOUNT_LIMIT = 5000.0;
    private final static int DAILY_COUNT_LIMIT = 3;
    private final LoadFundsTransactionRepository loadFundsTransactionRepository;

    public DailyFundsLoadValidator(LoadFundsTransactionRepository loadFundsTransactionRepository) {
        this.loadFundsTransactionRepository = loadFundsTransactionRepository;
    }

    @Override
    public boolean validate(LoadFundsRequest payload) {
        // We skip transaction if the amount is bigger than limit before fetching data from DB for optimization purposes
        if (payload.getLoadAmount() > DAILY_AMOUNT_LIMIT) return false;

        List<LoadFundsTransactionEntity> dailyTransactions = loadFundsTransactionRepository.findAllTodayById(payload.getCustomerId(), payload.getTime());
        return dailyCountLimitNotBreached(dailyTransactions) && dailyAmountLimitNotBreached(dailyTransactions, payload.getLoadAmount());
    }

    private boolean dailyCountLimitNotBreached(List<LoadFundsTransactionEntity> transactions){
        return transactions.size() < DAILY_COUNT_LIMIT;
    }

    private boolean dailyAmountLimitNotBreached(List<LoadFundsTransactionEntity> transactions, double loadAmount){
        return transactions.stream()
                .mapToDouble(LoadFundsTransactionEntity::getLoadAmount)
                .sum() + loadAmount < DAILY_AMOUNT_LIMIT;
    }
}
