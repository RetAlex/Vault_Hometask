package com.vault.hometask.service;

import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;

public interface FundsLoadValidator {
    /**
     * Validates if FundsLoad request is valid and can be processed. Depending on validator type it can check limit
     * breaches, transactions amount breaches etc.
     * @param payload The funds load request needed to be validated
     * @return true if request is valid and can be processed; false - otherwise
     *
     * To consider as enhancement - change boolean return type to validation error class to return more information about validation breach
     */
    ValidationResult validate(FundsLoadRequest payload);
}
