package com.ivanfranchin.customerservice.repository;

import com.ivanfranchin.customerservice.model.Customer;
import com.ivanfranchin.customerservice.model.Order;
import com.ivanfranchin.customerservice.MySQLTestcontainers;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ImportTestcontainers(MySQLTestcontainers.class)
class OrderRepositoryJpaTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveAndFindById() {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");
        entityManager.persist(customer);

        Order order = new Order();
        order.setId("o1");
        order.setRestaurantName("Pizza Place");
        order.setStatus("CREATED");
        order.setTotal(new BigDecimal("25.50"));
        order.setCreatedAt(ZonedDateTime.now());
        order.setCustomer(customer);
        orderRepository.save(order);

        Order found = orderRepository.findById("o1").orElseThrow();
        assertThat(found.getRestaurantName()).isEqualTo("Pizza Place");
        assertThat(found.getCustomer().getId()).isEqualTo("c1");
    }

    @Test
    void testFindByCustomerIdOrderByCreatedAtDesc() {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");
        entityManager.persist(customer);

        ZonedDateTime now = ZonedDateTime.now();

        Order o1 = new Order();
        o1.setId("o1");
        o1.setRestaurantName("Rest A");
        o1.setStatus("CREATED");
        o1.setTotal(new BigDecimal("10.00"));
        o1.setCreatedAt(now.minusHours(2));
        o1.setCustomer(customer);
        orderRepository.save(o1);

        Order o2 = new Order();
        o2.setId("o2");
        o2.setRestaurantName("Rest B");
        o2.setStatus("CONFIRMED");
        o2.setTotal(new BigDecimal("20.00"));
        o2.setCreatedAt(now.minusHours(1));
        o2.setCustomer(customer);
        orderRepository.save(o2);

        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc("c1");
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getId()).isEqualTo("o2");
        assertThat(orders.get(1).getId()).isEqualTo("o1");
    }
}
