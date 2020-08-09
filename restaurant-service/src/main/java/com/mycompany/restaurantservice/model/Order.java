package com.mycompany.restaurantservice.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
@ToString(exclude = "restaurant")
@EqualsAndHashCode(exclude = "restaurant")
@Entity
@Table(name = "orders")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Order {

    @Id
    private String id;
    private String customerName;
    private String customerAddress;
    private String status;
    private BigDecimal total = BigDecimal.ZERO;
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Set<OrderItem> items;

}
