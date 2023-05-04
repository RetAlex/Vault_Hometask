package com.vault.hometask.service.impl;

import com.vault.hometask.entity.FundsLoadTransactionEntity;
import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.service.FundsLoader;
import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;
import com.vault.hometask.util.enums.ValidationFailure;
import com.vault.hometask.util.exceptions.DuplicateTransactionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundsLoaderImplTest {
    @Mock
    private FundsLoadTransactionRepository fundsLoadTransactionRepositoryMock;
    @Mock
    private FundsLoadValidator firstFundsLoadValidatorMock, secondFundsLoadValidatorMock;

    private FundsLoader sut;

    @BeforeEach
    public void setup(){
        sut = new FundsLoaderImpl(List.of(firstFundsLoadValidatorMock, secondFundsLoadValidatorMock), fundsLoadTransactionRepositoryMock);
    }

    @Test
    void loadFundsPositiveCase() throws ParseException, DuplicateTransactionException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findById(testRequest.getId())).thenReturn(Optional.empty());
        when(firstFundsLoadValidatorMock.validate(testRequest)).thenReturn(new ValidationResult(true, null));
        when(secondFundsLoadValidatorMock.validate(testRequest)).thenReturn(new ValidationResult(true, null));

        assertTrue(sut.loadFunds(testRequest));
    }

    @Test
    void loadFundsNegativeCase() throws ParseException, DuplicateTransactionException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findById(testRequest.getId())).thenReturn(Optional.empty());
        when(secondFundsLoadValidatorMock.validate(testRequest)).thenReturn(new ValidationResult(false, ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED));
        when(firstFundsLoadValidatorMock.validate(testRequest)).thenReturn(new ValidationResult(true, null));

        assertFalse(sut.loadFunds(testRequest));
    }

    @Test
    void loadFundsDuplicateTransactionException() throws ParseException, DuplicateTransactionException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findById(testRequest.getId())).thenReturn(Optional.of(new FundsLoadTransactionEntity()));

        assertThrows(DuplicateTransactionException.class, () -> sut.loadFunds(testRequest));
    }
}