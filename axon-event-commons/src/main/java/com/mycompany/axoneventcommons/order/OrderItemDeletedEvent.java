package com.mycompany.axoneventcommons.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDeletedEvent implements OrderEvent {

    private String orderId;
    private String itemId;

}
