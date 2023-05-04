package com.vault.hometask.util.dto;

import com.vault.hometask.util.enums.ValidationFailure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResult {
    private boolean requestValid;
    private ValidationFailure failureReason;

}
