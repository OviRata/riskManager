package com.example.riskManager.messaging;

import com.example.riskManager.domain.Trade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TradeProducer {

    // KafkaTemplate is provided by Spring Boot automatically
    private final KafkaTemplate<String, Trade> kafkaTemplate;

    private final Random random = new Random();
    private final List<String> symbols = List.of("AAPL", "MSFT", "TSLA", "GOOGL", "AMZN");

    @Scheduled(fixedRate = 1000) // Runs every 1000 milliseconds (1 second)
    public void simulateTradeStream() {

        String symbol = symbols.get(random.nextInt(symbols.size()));
        int quantity = random.nextInt(100) + 1; // 1 to 100 shares

        // Random price between 150.00 and 200.00
        double rawPrice = random.nextInt(10) == 0 ? -50.0 : 150.0 + random.nextDouble() * 50.0;
        BigDecimal price = BigDecimal.valueOf(rawPrice).setScale(2, RoundingMode.HALF_UP);

        Trade.Side side = random.nextBoolean() ? Trade.Side.BUY : Trade.Side.SELL;
        String tradeId = UUID.randomUUID().toString();

        Trade trade = new Trade(tradeId, symbol, quantity, price, side);

        // Send the original trade
        kafkaTemplate.send("trades", symbol, trade);
        log.info("Published trade: {}", trade);

        // 10% chance to simulate a network glitch and send the EXACT SAME trade again
        if (random.nextInt(10) == 0) {
            log.warn("Simulating Kafka network retry... resending trade: {}", trade.tradeId());
            kafkaTemplate.send("trades", symbol, trade);
        }

        log.info("Published trade: {}", trade);
    }
}