package com.mycompany.foodorderingservice.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDeletedEvent {

    private String orderId;
    private String itemId;

}
