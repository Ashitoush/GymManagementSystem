package com.gymManagement.repo;

import com.gymManagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<Payment> findTopByUserIdOrderByPaymentDateDesc(Long userId);
    @Query(value = "Select * from payment Where user_id_fk = ?1 And payment_date = ?2", nativeQuery = true)
    List<Payment> findByDateAndUserId(Long userId, LocalDate date);
    List<Payment> findByPaymentDate(LocalDate date);
}
