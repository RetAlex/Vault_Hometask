package com.vault.hometask.controller;

import com.vault.hometask.controller.models.LoadFundsPayload;
import com.vault.hometask.controller.models.LoadFundsResponse;
import com.vault.hometask.service.FundsLoader;
import com.vault.hometask.util.dto.LoadFundsRequest;
import com.vault.hometask.util.exceptions.DuplicateTransactionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/funds")
public class LoadFundsController {
    private final FundsLoader fundsLoader;

    public LoadFundsController(FundsLoader fundsLoader) {
        this.fundsLoader = fundsLoader;
    }

    @PostMapping("/load")
    public LoadFundsResponse loadFunds(@RequestBody LoadFundsPayload payload){
        try {
            boolean fundsLoaded = fundsLoader.loadFunds(new LoadFundsRequest(payload));
            return new LoadFundsResponse(payload.getId(), payload.getCustomerId(), fundsLoaded);
        } catch (DuplicateTransactionException ex){
            log.warn("Skipping transaction with id %d because it already existed".formatted(payload.getId()));
            return null;
        }
    }
}
