package com.vault.hometask.service.impl;

import com.vault.hometask.entity.FundsLoadTransactionEntity;
import com.vault.hometask.repository.FundsLoadTransactionRepository;
import com.vault.hometask.service.FundsLoadValidator;
import com.vault.hometask.service.FundsLoader;
import com.vault.hometask.util.Constants;
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
import java.util.List;

import static com.vault.hometask.util.Constants.DAILY_AMOUNT_LIMIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyFundsLoadValidatorTest {
    @Mock
    private FundsLoadTransactionRepository fundsLoadTransactionRepositoryMock;

    @InjectMocks
    private DailyFundsLoadValidator sut;
    @Test
    void validateSuccess() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findAllTodayById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(List.of());

        ValidationResult result = sut.validate(testRequest);

        assertTrue(result.isRequestValid());
        assertNull(result.getFailureReason());
    }

    @Test
    void validateSuccessTransactionsExist() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findAllTodayById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(List.of(
                        new FundsLoadTransactionEntity(testRequest.getId()+1, testRequest.getCustomerId(), 1.0, testRequest.getTime()),
                        new FundsLoadTransactionEntity(testRequest.getId()+2, testRequest.getCustomerId(), 1.0, testRequest.getTime()))
                );

        ValidationResult result = sut.validate(testRequest);

        assertTrue(result.isRequestValid());
        assertNull(result.getFailureReason());
    }

    @Test
    void validateCountFailure() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findAllTodayById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(List.of(
                        new FundsLoadTransactionEntity(testRequest.getId()+1, testRequest.getCustomerId(), 1.0, testRequest.getTime()),
                        new FundsLoadTransactionEntity(testRequest.getId()+2, testRequest.getCustomerId(), 1.0, testRequest.getTime()),
                        new FundsLoadTransactionEntity(testRequest.getId()+3, testRequest.getCustomerId(), 1.0, testRequest.getTime()))
                );

        ValidationResult result = sut.validate(testRequest);

        assertFalse(result.isRequestValid());
        assertEquals(ValidationFailure.TRANSACTION_COUNT_LIMIT_REACHED, result.getFailureReason());
    }

    @Test
    void validateSingleAmountFailure() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, DAILY_AMOUNT_LIMIT+1, transactionDate);

        ValidationResult result = sut.validate(testRequest);

        assertFalse(result.isRequestValid());
        assertEquals(ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED, result.getFailureReason());
    }

    @Test
    void validateAmountFailure() throws ParseException {
        Date transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-02T00:00:00Z");
        FundsLoadRequest testRequest = new FundsLoadRequest(1, 12, 123.45, transactionDate);

        when(fundsLoadTransactionRepositoryMock.findAllTodayById(testRequest.getCustomerId(), testRequest.getTime()))
                .thenReturn(List.of(
                        new FundsLoadTransactionEntity(testRequest.getId()+1, testRequest.getCustomerId(), DAILY_AMOUNT_LIMIT-1, testRequest.getTime()))
                );

        ValidationResult result = sut.validate(testRequest);

        assertFalse(result.isRequestValid());
        assertEquals(ValidationFailure.TRANSACTION_AMOUNT_EXCEEDED, result.getFailureReason());
    }
}