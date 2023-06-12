//package com.gymManagement.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "user_schedule")
//public class UserSchedule {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_schedule_id")
//    private Long userScheduleId;
//
//    @ManyToOne
//    @JoinColumn(name = "schedule_id_fk")
//    private Schedule schedule;
//    @ManyToOne
//    @JoinColumn(name = "user_id_fk")
//    private User user;
//}
