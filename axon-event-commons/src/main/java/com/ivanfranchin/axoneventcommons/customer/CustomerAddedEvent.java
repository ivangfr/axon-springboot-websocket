package com.ivanfranchin.axoneventcommons.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddedEvent implements CustomerEvent {

    private String id;
    private String name;
    private String address;
}
