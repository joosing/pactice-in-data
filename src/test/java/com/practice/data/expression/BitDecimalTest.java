package com.practice.data.expression;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class BitDecimalTest {
    @Test
    void sumInt() {
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("3");
        BigDecimal c = a.add(b);
        assertEquals(4, c.intValue());
    }

    @Test
    void divideInt() {
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("3");
        BigDecimal c = a.divide(b, 3, RoundingMode.HALF_DOWN);
        assertEquals(0.333, c.doubleValue(), 0.001);
    }
}
