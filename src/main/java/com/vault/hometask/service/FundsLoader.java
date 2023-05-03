package com.vault.hometask.service;

import com.vault.hometask.util.dto.LoadFundsRequest;
import com.vault.hometask.util.exceptions.DuplicateTransactionException;

public interface FundsLoader {
    /**
     * Loads funds into the account if possible. Before saving the transaction it is validated against current set of
     * rules. Duplicated transactions (with the same id) will throw @{@link com.vault.hometask.util.exceptions.DuplicateTransactionException}
     * @param payload the load funds transaction to be saved
     * @return boolean indicating if transaction was saved successfully
     */
    boolean loadFunds(LoadFundsRequest payload) throws DuplicateTransactionException;
}
