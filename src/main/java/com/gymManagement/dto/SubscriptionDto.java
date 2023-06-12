package com.gymManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long subscriptionId;
    private String subscriptionName;
    private String subscriptionDescription;
    private String duration;
    private Long amount;
}
