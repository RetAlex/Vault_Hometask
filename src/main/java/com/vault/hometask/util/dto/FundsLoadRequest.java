package com.vault.hometask.util.dto;

import com.vault.hometask.controller.models.FundsLoadPayload;
import com.vault.hometask.util.CurrencyParser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FundsLoadRequest {
    private long id;
    private long customerId;
    private double loadAmount;
    private Date time;

    public FundsLoadRequest(FundsLoadPayload fundsLoadPayload){
        this.id = fundsLoadPayload.getId();
        this.customerId = fundsLoadPayload.getCustomerId();
        this.time = fundsLoadPayload.getTime();
        this.loadAmount = CurrencyParser.currencyToDouble(fundsLoadPayload.getLoadAmount());
    }
}
