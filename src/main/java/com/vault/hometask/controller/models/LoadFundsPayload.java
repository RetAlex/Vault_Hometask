package com.vault.hometask.controller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadFundsPayload {
    private long id;
    @JsonProperty(value = "customer_id")
    private long customerId;
    @JsonProperty(value = "load_amount")
    private String loadAmount;
    private Date time;
}
