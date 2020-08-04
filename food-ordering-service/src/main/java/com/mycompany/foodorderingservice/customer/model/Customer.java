package com.mycompany.foodorderingservice.customer.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private String id;
    private String name;
    private String address;

}
