package com.vault.hometask.service.impl;

import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.util.dto.FundsLoadRequest;
import com.vault.hometask.util.dto.ValidationResult;
import com.vault.hometask.util.enums.ValidationFailure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.vault.hometask.util.Constants.WEEKLY_AMOUNT_LIMIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeeklyFundLoadValidatorTest {
    @Mock
    private FundsLoadTransactionRepository fundsLoadTransactionRepositoryMock;

    @InjectMocks
    private WeeklyFundLoadValidator sut;
    @Test
    void validateSuccess() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, WEEKLY_AMOUNT_LIMIT-1, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findWeeklyTransactionSumById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(Optional.empty());

        ValidationResult result = sut.validate(testRequest);

        assertTrue(result.isRequestValid());
        assertNull(result.getFailureReason());
    }

    @Test
    void validateSuccessTransactionsExist() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, WEEKLY_AMOUNT_LIMIT-2, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findWeeklyTransactionSumById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(Optional.of(1.0));

        ValidationResult result = sut.validate(testRequest);

        assertTrue(result.isRequestValid());
        assertNull(result.getFailureReason());
    }

    @Test
    void validateSingleAmountFailure() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, WEEKLY_AMOUNT_LIMIT+1, transactionDate);

        ValidationResult result = sut.validate(testRequest);

        assertFalse(result.isRequestValid());
        assertEquals(ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED, result.getFailureReason());
    }

    @Test
    void validateAmountFailure() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findWeeklyTransactionSumById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(Optional.of(WEEKLY_AMOUNT_LIMIT-1));

        ValidationResult result = sut.validate(testRequest);

        assertFalse(result.isRequestValid());
        assertEquals(ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED, result.getFailureReason());
    }
}