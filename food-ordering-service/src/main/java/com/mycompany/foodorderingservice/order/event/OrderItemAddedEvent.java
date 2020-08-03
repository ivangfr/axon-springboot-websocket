package com.mycompany.foodorderingservice.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAddedEvent {

    private String orderId;
    private String itemId;
    private Float itemPrice;
    private Short quantity;

}
