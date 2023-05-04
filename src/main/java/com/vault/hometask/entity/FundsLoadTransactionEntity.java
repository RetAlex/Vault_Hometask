package com.vault.hometask.entity;

import com.vault.hometask.util.dto.FundsLoadRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "load_funds_transactions")
@Entity
public class FundsLoadTransactionEntity {
    @Id
    @Column(name="id")
    private long id;
    @Column(name="customer_id")
    private long customerId;
    @Column(name="load_amount")
    private double loadAmount;
    @Column(name="transaction_time")
    private Date time;

    public FundsLoadTransactionEntity(FundsLoadRequest payload){
        this.id = payload.getId();
        this.loadAmount = payload.getLoadAmount();
        this.customerId = payload.getCustomerId();
        this.time = payload.getTime();
    }
}
