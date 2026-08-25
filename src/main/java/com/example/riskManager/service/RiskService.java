package com.example.riskManager.service;

import com.example.riskManager.domain.RiskExposure;
import com.example.riskManager.domain.Trade;
import com.example.riskManager.repository.RiskExposureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RiskService {

    private final RiskExposureRepository repository;
    private final SimpMessagingTemplate messagingTemplate; // ADD THIS

    @Transactional
    public void processTrade(Trade trade) {
        // SIMULATED BUGS: Throw an exception if the system receives a negative price
        if (trade.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fatal: Trade price cannot be negative!");
        }

        RiskExposure exposure = repository.findByIdForUpdate(trade.symbol())
                .orElseGet(() -> new RiskExposure(trade.symbol(), 0L, BigDecimal.ZERO));

        long positionChange = trade.side() == Trade.Side.BUY ? trade.quantity() : -trade.quantity();
        BigDecimal tradeValue = trade.price().multiply(BigDecimal.valueOf(trade.quantity()));
        BigDecimal capitalChange = trade.side() == Trade.Side.BUY ? tradeValue : tradeValue.negate();

        exposure.setNetPosition(exposure.getNetPosition() + positionChange);
        exposure.setTotalCapitalTiedUp(exposure.getTotalCapitalTiedUp().add(capitalChange));

        // Save to DB
        RiskExposure savedExposure = repository.save(exposure);

        // ADD THIS: Push the updated exposure to all subscribed clients instantly
        messagingTemplate.convertAndSend("/topic/riskUpdates", savedExposure);
    }
}