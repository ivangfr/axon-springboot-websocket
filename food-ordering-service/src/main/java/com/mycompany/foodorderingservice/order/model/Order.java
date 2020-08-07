package com.mycompany.foodorderingservice.order.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Data
@ToString(exclude = "items")
@EqualsAndHashCode(exclude = "items")
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal total = BigDecimal.ZERO;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String restaurantId;
    private String restaurantName;

    //@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items;

}
