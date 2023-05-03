package com.vault.hometask.util;

public class CurrencyParser {
    public static double currencyToDouble(String currencyString){
        if (!Character.isDigit(currencyString.charAt(0)))
            return Double.parseDouble(currencyString.substring(1));
        return Double.parseDouble(currencyString);
    }
}
