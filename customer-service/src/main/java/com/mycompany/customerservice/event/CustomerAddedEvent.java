package com.mycompany.customerservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddedEvent {

    private String id;
    private String name;
    private String address;

}
