package com.vault.hometask.util.dto;

import com.vault.hometask.controller.models.LoadFundsPayload;
import com.vault.hometask.util.CurrencyParser;
import lombok.Data;

import java.util.Date;

@Data
public class LoadFundsRequest {
    private long id;
    private long customerId;
    private double loadAmount;
    private Date time;

    public LoadFundsRequest(LoadFundsPayload loadFundsPayload){
        this.id = loadFundsPayload.getId();
        this.customerId = loadFundsPayload.getCustomerId();
        this.time = loadFundsPayload.getTime();
        this.loadAmount = CurrencyParser.currencyToDouble(loadFundsPayload.getLoadAmount());
    }
}
