package com.vault.hometask.util;

public class CurrencyParser {
    /**
     * Simple currency parser. Accepts either double number with no currency or with a single currency symbol in front of the number.
     * @param currencyString string representation of the amount; Either double number (ex 123.45) or double number with symbol in front (ex $123.45)
     * @throws NumberFormatException if currencyString is in neither accepted format
     * @return double number stored in the input string
     */
    public static double currencyToDouble(String currencyString){
        String trimmedInput = currencyString.trim();
        if (!Character.isDigit(trimmedInput.charAt(0)))
            return Double.parseDouble(trimmedInput.substring(1).trim());
        return Double.parseDouble(trimmedInput);
    }
}
