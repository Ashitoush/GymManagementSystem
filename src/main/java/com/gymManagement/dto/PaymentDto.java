package com.gymManagement.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PaymentDto {
    private Long paymentId;
    private Long amount;
    private Long dueAmount;
    private Long paidAmount;
    private LocalDate paymentDate;
    private String nameOfUser;
    private Long userId;
}
