package com.vault.hometask.service.impl;

import com.vault.hometask.controller.models.LoadFundsPayload;
import com.vault.hometask.entity.LoadFundsTransactionEntity;
import com.vault.hometask.repository.LoadFundsTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.service.FundsLoader;
import com.vault.hometask.util.dto.LoadFundsRequest;
import com.vault.hometask.util.exceptions.DuplicateTransactionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundsLoaderImpl implements FundsLoader {
    private final List<FundsLoadValidator> validatorList;
    private final LoadFundsTransactionRepository loadFundsTransactionRepository;

    public FundsLoaderImpl(List<FundsLoadValidator> validatorList, LoadFundsTransactionRepository loadFundsTransactionRepository) {
        this.validatorList = validatorList;
        this.loadFundsTransactionRepository = loadFundsTransactionRepository;
    }

    @Override
    public boolean loadFunds(LoadFundsRequest payload) throws DuplicateTransactionException {
        if (!isNewTransaction(payload.getId())) throw new DuplicateTransactionException();
        boolean validTransaction = validatorList.stream()
                .map(validator -> validator.validate(payload))
                .filter(validationResult -> !validationResult)
                .findAny().isEmpty();

        if(validTransaction)
            loadFundsTransactionRepository.save(new LoadFundsTransactionEntity(payload));

        return validTransaction;
    }

    private boolean isNewTransaction(long transactionId){
        return loadFundsTransactionRepository.findById(transactionId).isEmpty();
    }
}
