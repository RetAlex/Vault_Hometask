package com.vault.hometask.service.impl;

import com.vault.hometask.entity.FundsLoadTransactionEntity;
import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.service.FundsLoader;
import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;
import com.vault.hometask.util.exceptions.DuplicateTransactionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class FundsLoaderImpl implements FundsLoader {
    private final List<FundsLoadValidator> validatorList;
    private final FundsLoadTransactionRepository fundsLoadTransactionRepository;

    public FundsLoaderImpl(List<FundsLoadValidator> validatorList, FundsLoadTransactionRepository fundsLoadTransactionRepository) {
        this.validatorList = validatorList;
        this.fundsLoadTransactionRepository = fundsLoadTransactionRepository;
    }

    @Override
    public boolean loadFunds(FundsLoadRequest payload) throws DuplicateTransactionException {
        if (!isNewTransaction(payload.getId())) throw new DuplicateTransactionException();
        boolean validTransaction = validatorList.stream()
                .map(validator -> validator.validate(payload))
                .filter(validationResult -> !validationResult.isRequestValid())
                .peek(validationResult -> logBreach(payload, validationResult))
                .findAny().isEmpty();

        if(validTransaction)
            fundsLoadTransactionRepository.save(new FundsLoadTransactionEntity(payload));

        return validTransaction;
    }

    private void logBreach(FundsLoadRequest request, ValidationResult validationResult){
        log.warn("Transaction %s can't be processed because it breached the following failure reason: %s".formatted(request.getId(), validationResult.getFailureReason()));
    }

    private boolean isNewTransaction(long transactionId){
        return fundsLoadTransactionRepository.findById(transactionId).isEmpty();
    }
}
