package com.vault.hometask.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyParserTest {

    @Test
    void currencyToDoublePositiveInputNumberCase() {
        assertEquals(123.45, CurrencyParser.currencyToDouble("123.45"));
    }

    @Test
    void currencyToDoubleNegativeInputCase() {
        assertThrows(NumberFormatException.class, () -> CurrencyParser.currencyToDouble("123.45.123"));
    }

    @Test
    void currencyToDoublePositiveSymbolFirst() {
        assertEquals(123.45, CurrencyParser.currencyToDouble("$123.45"));
    }

    @Test
    void currencyToDoubleNegativeSymbolFirst() {
        assertThrows(NumberFormatException.class, () -> CurrencyParser.currencyToDouble("$!123.45"));
    }

    @Test
    void currencyToDoublePositiveSymbolFirstWithSpaces() {
        assertEquals(123.45, CurrencyParser.currencyToDouble(" $ 123.45 "));
    }

    @Test
    void currencyToDoublePositiveWithSpaces() {
        assertEquals(123.45, CurrencyParser.currencyToDouble(" 123.45  "));
    }
}