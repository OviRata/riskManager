package com.example.riskManager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskExposure {

    @Id
    private String symbol;

    private long netPosition; // Positive means we own it (long), negative means we owe it (short)
    private BigDecimal totalCapitalTiedUp;
}