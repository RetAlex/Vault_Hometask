package com.vault.hometask.repository;

import com.vault.hometask.entity.FundsLoadTransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FundsLoadTransactionRepository extends CrudRepository<FundsLoadTransactionEntity, Long> {
    @Query(value = "select * from load_funds_transactions where customer_id = ?1 " +
            "AND DATE_TRUNC(DAY, transaction_time) = DATE_TRUNC(DAY, CAST(?2 as DATE))", nativeQuery = true)
    List<FundsLoadTransactionEntity> findAllTodayById(long customerId, Date transactionDate);
    @Query(value = "select sum(load_amount) from load_funds_transactions where customer_id = ?1 " +
            "AND DATE_TRUNC(ISO_WEEK, transaction_time) = DATE_TRUNC(ISO_WEEK, CAST(?2 as DATE))", nativeQuery = true)
    Optional<Double> findWeeklyTransactionSumById(long customerId, Date transactionDate);
}
