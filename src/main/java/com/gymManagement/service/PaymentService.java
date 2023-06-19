package com.gymManagement.service;

import com.gymManagement.dto.PaymentDto;
import com.gymManagement.dto.SearchDto;
import com.gymManagement.model.Payment;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    PaymentDto getPaymentById(Long paymentId);
    List<Payment> getPaymentsByUserId(Long userId);
//List<PaymentDto> getPaymentsByUser();
    String deletePayment(Long paymentId) throws Exception;
    Double getTotalAmountCollectedThisMonth();
    Map<String, Object> calculateDueAmount(Principal principal);
    Map<String, Object> getLastPaymentAmount(Principal principal);
    Double getTotalDueAmount();
    List<Payment> searchPaymentByDate(Long userId, SearchDto searchDto);
    PaymentDto convertToDto(Payment payment);
    List<PaymentDto> convertToDto(List<Payment> payments);
    List<Payment> convertToEntity(List<PaymentDto> paymentDtos);
}
