package com.gymManagement.repo;

import com.gymManagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<Payment> findTopByUserIdOrderByPaymentDateDesc(Long userId);
}
