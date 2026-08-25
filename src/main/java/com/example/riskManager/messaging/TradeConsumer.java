package com.example.riskManager.messaging;

import com.example.riskManager.domain.Trade;
import com.example.riskManager.service.RiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TradeConsumer {

    private final RiskService riskService;

    @KafkaListener(topics = "trades", groupId = "risk-engine-group")
    public void consume(Trade trade) {
        log.info("Received trade from Kafka: {} {} @ {}",
                trade.side(), trade.quantity(), trade.price());

        // We removed the try-catch block!
        // If processTrade throws an exception, Spring will intercept it.
        riskService.processTrade(trade);
    }

    @KafkaListener(topics = "trades.DLT", groupId = "risk-dlq-group")
    public void consumeDlq(Trade trade) {
        log.error("⚠️ DLQ ALERT: Trade failed processing and was sent to DLT: {}", trade.tradeId());
        // In a real enterprise, this might send a Slack alert to the engineering team
        // or insert the failed record into an 'investigation_required' database table.
    }

}