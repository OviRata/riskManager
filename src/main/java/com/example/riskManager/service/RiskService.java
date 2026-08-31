package com.example.riskManager.service;

import com.example.riskManager.domain.ProcessedTrade;
import com.example.riskManager.domain.RiskExposure;
import com.example.riskManager.domain.Trade;
import com.example.riskManager.repository.ProcessedTradeRepository;
import com.example.riskManager.repository.RiskExposureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;


@Service
@RequiredArgsConstructor
@Slf4j
public class RiskService {

    private final RiskExposureRepository riskRepository;
    private final ProcessedTradeRepository processedTradeRepository;
    private final SimpMessagingTemplate messagingTemplate; // ADD THIS

    @Transactional
    public void processTrade(Trade trade) {

        if (processedTradeRepository.existsById(trade.tradeId())) {
            log.warn("🔄 IDEMPOTENCY TRIGGERED: Trade {} was already processed. Skipping.", trade.tradeId());
            return; // Gracefully drop the duplicate message
        }

        // SIMULATED BUGS: Throw an exception if the system receives a negative price
        if (trade.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fatal: Trade price cannot be negative!");
        }

        // 2. Fetch and lock the exposure
        RiskExposure exposure = riskRepository.findByIdForUpdate(trade.symbol())
                .orElseGet(() -> new RiskExposure(trade.symbol(), 0L, BigDecimal.ZERO));

        // 3. Calculate changes
        long positionChange = trade.side() == Trade.Side.BUY ? trade.quantity() : -trade.quantity();
        BigDecimal tradeValue = trade.price().multiply(BigDecimal.valueOf(trade.quantity()));
        BigDecimal capitalChange = trade.side() == Trade.Side.BUY ? tradeValue : tradeValue.negate();

        exposure.setNetPosition(exposure.getNetPosition() + positionChange);
        exposure.setTotalCapitalTiedUp(exposure.getTotalCapitalTiedUp().add(capitalChange));

        // 4. Save the new exposure
        RiskExposure savedExposure = riskRepository.save(exposure);

        // 5. Mark the trade as processed in the SAME transaction
        processedTradeRepository.save(new ProcessedTrade(trade.tradeId(), Instant.now()));

        // 6. Broadcast to dashboard
        messagingTemplate.convertAndSend("/topic/riskUpdates", savedExposure);
    }
}