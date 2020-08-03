package com.mycompany.foodorderingservice.order.event;

import com.mycompany.foodorderingservice.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOpenedEvent {

    private String orderId;
    private String customerId;
    private String restaurantId;
    private OrderStatus status;

}
