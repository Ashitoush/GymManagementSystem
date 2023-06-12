package com.gymManagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "due_amount")
    private Long dueAmount;
    @Column(name = "paid_amount")
    private Long paidAmount;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    @JsonBackReference(value = "user")
    private User user;


}
