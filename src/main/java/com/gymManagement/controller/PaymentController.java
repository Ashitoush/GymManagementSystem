package com.gymManagement.controller;

import com.gymManagement.dto.PaymentDto;
import com.gymManagement.helper.MergeSort;
import com.gymManagement.model.Payment;
import com.gymManagement.model.User;
import com.gymManagement.repo.PaymentRepo;
import com.gymManagement.repo.UserRepo;
import com.gymManagement.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MergeSort mergeSort;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_payment')")
    public Map<String, Object> createPayment(@RequestBody PaymentDto paymentDto) {
        Map<String, Object> message = new HashMap<>();
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        if(createdPayment != null) {
            message.put("status", 200);
            message.put("message", "payment created");
            message.put("data", createdPayment);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while creating payment");
        }
        return message;
    }

    @PutMapping("/update/{paymentId}")
    @PreAuthorize("hasAuthority('update_payment')")
    public Map<String, Object> updatePayment(@PathVariable Long paymentId, @RequestBody PaymentDto paymentDto) {
        Map<String, Object> message = new HashMap<>();
        PaymentDto updatedPayment = paymentService.updatePayment(paymentId, paymentDto);
        if (updatedPayment != null) {
            message.put("status", 200);
            message.put("message", "payment updated");
            message.put("data", updatedPayment);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while updating payment");
        }

        return message;
    }

//    @GetMapping("/getAllPayment")
//    public ResponseEntity<List<PaymentDto>> getAllPayments() {
//        List<PaymentDto> payments = paymentService.getAllPayments();
//        return ResponseEntity.ok(payments);
//    }

    @GetMapping("/getAllPayment")
    @PreAuthorize("hasAuthority('view_all_payment')")
    public Map<String, Object> getAllPayments(Principal principal) {
        Map<String, Object> message = new HashMap<>();

        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();

        List<Payment> payments = this.paymentRepo.findAll();
        Comparator<Payment> comparator = Comparator.comparing(Payment::getPaymentDate).reversed();
        payments = this.mergeSort.mergeSort(payments, comparator);

        List<PaymentDto> paymentDtos = payments.stream()
                .map(payment -> this.modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());

        if (!paymentDtos.isEmpty()) {
            message.put("status", 200);
            message.put("message", "retrieving payments");
            message.put("data", paymentDtos);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving payments");
        }
        message.put("fullName", fullName);
        return message;
    }


    @GetMapping("/getPaymentById/{paymentId}")
    @PreAuthorize("hasAuthority('view_payment')")
    public Map<String, Object> getPaymentById(@PathVariable Long paymentId) {
        Map<String, Object> message = new HashMap<>();
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            message.put("status", 200);
            message.put("message", "retrieved payment");
            message.put("data", payment);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving payment");
        }

        return message;
    }

    @GetMapping("/getPaymentsByUserId")
    @PreAuthorize("hasAuthority('view_payment')")
    public Map<String, Object> getPaymentsByUserId(Principal principal) {
        Map<String, Object> message = new HashMap<>();
        User loggedInUser = this.userRepo.findByEmail(principal.getName());
        String fullName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();

        List<Payment> payments = paymentService.getPaymentsByUserId(loggedInUser.getId());

        Comparator<Payment> comparator = Comparator.comparing(Payment::getPaymentDate).reversed();
        payments = this.mergeSort.mergeSort(payments, comparator);

        List<PaymentDto> paymentDtos = payments.stream()
                .map(payment -> this.modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());

        if(!paymentDtos.isEmpty()) {
            message.put("status", 200);
            message.put("message", "retrieving payment");
            message.put("fullName", fullName);
            message.put("data", paymentDtos);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving payment");
        }
        return message;
    }
//@GetMapping("/getPaymentsByUserId")
//public ResponseEntity<List<PaymentDto>> getPaymentsByUserId() {
//    List<PaymentDto> payments = paymentService.getPaymentsByUser();
//    return ResponseEntity.ok(payments);
//}

    @DeleteMapping("/delete/{paymentId}")
    @PreAuthorize("hasAuthority('delete_payment')")
    public Map<String, Object> deletePayment(@PathVariable Long paymentId) throws Exception {
        Map<String, Object> message = new HashMap<>();

        String result = paymentService.deletePayment(paymentId);

        if(!result.isEmpty()) {
            message.put("status", 200);
            message.put("message", result);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while deleting payment");
        }
        return message;
    }

    @GetMapping("/totalAmountCollected")
    @PreAuthorize("hasAuthority('view_total_amount_collected')")
    public Map<String, Object> getTotalAmountCollectedThisMonth() {
        Map<String, Object> message = new HashMap<>();
        Double totalAmount = paymentService.getTotalAmountCollectedThisMonth();
        if(totalAmount >= 0.0) {
            message.put("status", 200);
            message.put("message", "total amount collected this month");
            message.put("data", totalAmount);
        } else {
            message.clear();
            message.put("status", 500);
            message.put("message", "error while retrieving total amount collected this month");
        }
        return message;
    }

}
