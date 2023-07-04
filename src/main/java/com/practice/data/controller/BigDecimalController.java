package com.practice.data.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BigDecimalController {
    @GetMapping("/big-decimal/{num}")
    public CustomNumber bigDecimal(@PathVariable BigDecimal num) {
        return new CustomNumber(num);
    }

    @Value
    static class CustomNumber {
        BigDecimal num;
    }
}
