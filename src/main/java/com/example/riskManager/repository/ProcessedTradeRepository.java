package com.example.riskManager.repository;

import com.example.riskManager.domain.ProcessedTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedTradeRepository extends JpaRepository<ProcessedTrade, String> {
}