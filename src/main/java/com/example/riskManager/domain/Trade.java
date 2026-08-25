package com.example.riskManager.domain;

import java.math.BigDecimal;

public record Trade(
        String tradeId,
        String symbol,
        int quantity,
        BigDecimal price,
        Side side
) {
    public enum Side {
        BUY, SELL
    }
}