package com.gymManagement.service.implementation;

import com.gymManagement.dto.PaymentDto;
import com.gymManagement.model.Payment;
import com.gymManagement.model.User;
import com.gymManagement.repo.PaymentRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.security.UserPrincipal;
import com.gymManagement.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = modelMapper.map(paymentDto, Payment.class);
//        User user = this.userRepo.findById(paymentDto.getUserId()).get();
        User user = this.userRepo.findByUserName(paymentDto.getNameOfUser());
        Long dueAmount = payment.getAmount() - payment.getPaidAmount();
        payment.setDueAmount(dueAmount);
        payment.setUser(user);
        Payment savedPayment = paymentRepo.save(payment);
        return convertToDto(savedPayment);
    }

    @Override
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) {
        Optional<Payment> optionalPayment = paymentRepo.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setAmount(paymentDto.getAmount());
            payment.setPaidAmount(paymentDto.getPaidAmount());
            Long dueAmount = paymentDto.getAmount() - paymentDto.getPaidAmount();
            payment.setDueAmount(dueAmount);
            payment.setPaymentDate(paymentDto.getPaymentDate());

            Payment updatedPayment = paymentRepo.save(payment);
            return modelMapper.map(updatedPayment, PaymentDto.class);
        }
        return null;
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepo.findAll();
        List<PaymentDto> paymentDtos = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentDto paymentDto = convertToDto(payment);
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }


    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepo.findById(paymentId);
        return optionalPayment.map(payment -> modelMapper.map(payment, PaymentDto.class)).orElse(null);
    }

    @Override
    public List<Payment> getPaymentsByUserId(Long userId) {
        List<Payment> payments = paymentRepo.findByUserId(userId);
        return payments;
    }

//    @Override
//    public List<PaymentDto> getPaymentsByUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getPrincipal() instanceof UserPrincipal) {
//            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//            String username = userPrincipal.getUsername();
//            User user = this.userRepo.findByUserName(username);
//            List<Payment> payments = paymentRepo.findByUserId(user.getId());
//            return payments.stream()
//                    .map(payment -> modelMapper.map(payment, PaymentDto.class))
//                    .collect(Collectors.toList());
//        } else {
//            // Handle the case when the user principal is not of the expected type
//            throw new IllegalStateException("Invalid user principal type.");
//        }
//    }


    @Override
    public String deletePayment(Long paymentId) throws Exception {
        if (paymentRepo.existsById(paymentId)) {
            Payment payment = this.paymentRepo.findById(paymentId).get();
            payment.setUser(null);
            paymentRepo.delete(payment);
            return "Payment deleted successfully";
        } else {
            throw new Exception("Payment not found with ID: " + paymentId);
        }
    }

    @Override
    public Double getTotalAmountCollectedThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        List<Payment> payments = paymentRepo.findByPaymentDateBetween(startDate, endDate);
        Double totalAmount = payments.stream()
                .mapToDouble(Payment::getPaidAmount)
                .sum();

        return totalAmount;
    }

    @Override
    public Double getTotalDueAmount() {
        List<Payment> payments = paymentRepo.findAll();
        Double totalDueAmount = payments.stream()
                .mapToDouble(Payment::getDueAmount).sum();
        return totalDueAmount;
    }

    @Override
    public Map<String, Object> calculateDueAmount(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());

        if (loggedInUser != null) {
            List<Payment> payments = paymentRepo.findByUserId(loggedInUser.getId());
            Double totalDueAmount = payments.stream()
                    .mapToDouble(Payment::getDueAmount)
                    .sum();

            message.put("status", 200);
            message.put("totalDueAmount", totalDueAmount);
        } else {
            message.put("status", 404);
            message.put("message", "User not found for the logged-in principal: ");
        }

        return message;
    }

    @Override
    public Map<String, Object> getLastPaymentAmount(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = userRepo.findByEmail(principal.getName());

        if (loggedInUser != null) {
            Optional<Payment> latestPayment = paymentRepo.findTopByUserIdOrderByPaymentDateDesc(loggedInUser.getId());

            if (latestPayment.isPresent()) {
                Payment lastPayment = latestPayment.get();
                Long lastPaymentAmount = lastPayment.getAmount();

                message.put("status", 200);
                message.put("lastPaymentAmount", lastPaymentAmount);
                message.put("lastPaymentDate", lastPayment.getPaymentDate());
            } else {
                message.put("status", 200);
                message.put("lastPaymentAmount", 0);
            }
        } else {
            message.put("status", 404);
            message.put("message", "User not found for the logged-in principal: " + principal.getName());
        }

        return message;
    }

    public PaymentDto convertToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setDueAmount(payment.getDueAmount());
        paymentDto.setPaidAmount(payment.getPaidAmount());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setNameOfUser(payment.getUser().getUserName());
        return paymentDto;
    }



}
