package com.mycompany.foodorderingservice.order.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
@Entity
@Table(name = "orders_items")
public class OrderItem {

    @Id
    private String id;
    private String dishId;
    private String dishName;
    private BigDecimal dishPrice;
    private Short quantity;

    //@ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
