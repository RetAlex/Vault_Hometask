package com.vault.hometask.service;

import com.vault.hometask.util.dto.LoadFundsRequest;

public interface FundsLoadValidator {
    boolean validate(LoadFundsRequest payload);
}
