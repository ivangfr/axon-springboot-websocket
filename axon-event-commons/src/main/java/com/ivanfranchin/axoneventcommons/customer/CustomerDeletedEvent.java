package com.ivanfranchin.axoneventcommons.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDeletedEvent implements CustomerEvent {

    private String id;
}
