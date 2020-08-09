package com.mycompany.customerservice.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@ToString(exclude = "customer")
@EqualsAndHashCode(exclude = "customer")
@Entity
@Table(name = "orders")
@TypeDef(name = "json", typeClass = JsonStringType.class)
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

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Set<OrderItem> items;

}
