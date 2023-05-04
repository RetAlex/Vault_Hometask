package com.vault.hometask.controller.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsLoadResponse {
    public long id;
    @JsonProperty(value = "customer_id")
    public long customerId;
    public boolean accepted;
}
