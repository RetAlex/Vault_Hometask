package com.vault.hometask.service.impl;

import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;
import com.vault.hometask.util.enums.ValidationFailure;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.vault.hometask.util.Constants.WEEKLY_AMOUNT_LIMIT;

@Service
public class WeeklyFundLoadValidator implements FundsLoadValidator {
    private final FundsLoadTransactionRepository fundsLoadTransactionRepository;

    public WeeklyFundLoadValidator(FundsLoadTransactionRepository fundsLoadTransactionRepository) {
        this.fundsLoadTransactionRepository = fundsLoadTransactionRepository;
    }

    @Override
    public ValidationResult validate(FundsLoadRequest payload) {
        // We skip transaction if the amount is bigger than limit before fetching data from DB for optimization purposes
        if (payload.getLoadAmount() > WEEKLY_AMOUNT_LIMIT || amountLimitBreached(payload.getCustomerId(), payload.getLoadAmount(), payload.getTime()))
            return new ValidationResult(false, ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED);

        return new ValidationResult(true, null);
    }

    private boolean amountLimitBreached(long customerId, double amount, Date date){
        return fundsLoadTransactionRepository.findWeeklyTransactionSumById(customerId, date).orElse(0.0)+amount > WEEKLY_AMOUNT_LIMIT;
    }
}
