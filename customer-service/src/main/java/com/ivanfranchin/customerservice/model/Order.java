package com.ivanfranchin.customerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@ToString(exclude = "customer")
@EqualsAndHashCode(exclude = "customer")
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;
    private String restaurantName;
    private String status;
    private BigDecimal total = BigDecimal.ZERO;
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JdbcTypeCode(SqlTypes.JSON)
    private Set<OrderItem> items;
}
