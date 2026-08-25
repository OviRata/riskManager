package com.example.riskManager.repository;

import com.example.riskManager.domain.RiskExposure;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskExposureRepository extends JpaRepository<RiskExposure, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RiskExposure r WHERE r.symbol = :symbol")
    Optional<RiskExposure> findByIdForUpdate(@Param("symbol") String symbol);
}