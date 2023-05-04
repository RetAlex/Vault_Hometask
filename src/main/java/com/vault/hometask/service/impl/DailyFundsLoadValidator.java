package com.vault.hometask.service.impl;

import com.vault.hometask.entity.FundsLoadTransactionEntity;
import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;
import com.vault.hometask.util.enums.ValidationFailure;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vault.hometask.util.Constants.DAILY_AMOUNT_LIMIT;
import static com.vault.hometask.util.Constants.DAILY_COUNT_LIMIT;

@Service
@Log4j2
public class DailyFundsLoadValidator implements FundsLoadValidator {
    private final FundsLoadTransactionRepository fundsLoadTransactionRepository;

    public DailyFundsLoadValidator(FundsLoadTransactionRepository fundsLoadTransactionRepository) {
        this.fundsLoadTransactionRepository = fundsLoadTransactionRepository;
    }

    @Override
    public ValidationResult validate(FundsLoadRequest payload) {
        // We skip transaction if the amount is bigger than limit before fetching data from DB for optimization purposes
        if (payload.getLoadAmount() > DAILY_AMOUNT_LIMIT)
            return new ValidationResult(false, ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED);

        List<FundsLoadTransactionEntity> dailyTransactions = fundsLoadTransactionRepository.findAllTodayById(payload.getCustomerId(), payload.getTime());

        if(dailyCountLimitBreached(dailyTransactions))
            return new ValidationResult(false, ValidationFailure.TRANSACTION_COUNT_LIMIT_REACHED);
        if(dailyAmountLimitBreached(dailyTransactions, payload.getLoadAmount()))
            return new ValidationResult(false, ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED);
        return new ValidationResult(true, null);
    }

    private boolean dailyCountLimitBreached(List<FundsLoadTransactionEntity> transactions){
        return transactions.size() >= DAILY_COUNT_LIMIT;
    }

    private boolean dailyAmountLimitBreached(List<FundsLoadTransactionEntity> transactions, double loadAmount){
        return transactions.stream()
                .mapToDouble(FundsLoadTransactionEntity::getLoadAmount)
                .sum() + loadAmount > DAILY_AMOUNT_LIMIT;
    }
}
