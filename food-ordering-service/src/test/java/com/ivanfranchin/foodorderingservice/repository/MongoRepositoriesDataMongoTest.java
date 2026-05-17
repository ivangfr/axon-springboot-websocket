package com.ivanfranchin.foodorderingservice.repository;

import com.ivanfranchin.foodorderingservice.customer.model.Customer;
import com.ivanfranchin.foodorderingservice.customer.repository.CustomerRepository;
import com.ivanfranchin.foodorderingservice.order.model.Order;
import com.ivanfranchin.foodorderingservice.order.model.OrderItem;
import com.ivanfranchin.foodorderingservice.order.model.OrderStatus;
import com.ivanfranchin.foodorderingservice.order.repository.OrderRepository;
import com.ivanfranchin.foodorderingservice.restaurant.model.Dish;
import com.ivanfranchin.foodorderingservice.restaurant.model.Restaurant;
import com.ivanfranchin.foodorderingservice.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MongoRepositoriesDataMongoTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testCustomerRepositorySaveAndFind() {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findById("c1");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John");

        List<Customer> all = customerRepository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    void testCustomerRepositoryDelete() {
        Customer customer = new Customer();
        customer.setId("c1");
        customer.setName("John");
        customer.setAddress("123 Main St");
        customerRepository.save(customer);

        customerRepository.deleteById("c1");
        assertThat(customerRepository.findById("c1")).isEmpty();
    }

    @Test
    void testOrderRepositorySaveAndFind() {
        Order order = new Order();
        order.setId("o1");
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("25.50"));
        order.setCustomerId("c1");
        order.setRestaurantName("Pizza Hut");

        OrderItem item = new OrderItem("d1", "Pizza Margherita", new BigDecimal("6.99"), (short) 2);
        order.setItems(Set.of(item));
        orderRepository.save(order);

        Optional<Order> found = orderRepository.findById("o1");
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(found.get().getItems()).hasSize(1);
    }

    @Test
    void testRestaurantRepositorySaveAndFind() {
        Dish dish = new Dish();
        dish.setId("d1");
        dish.setName("Pizza Margherita");
        dish.setPrice(new BigDecimal("6.99"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId("r1");
        restaurant.setName("Pizza Hut");
        restaurant.setDishes(Set.of(dish));
        restaurantRepository.save(restaurant);

        Optional<Restaurant> found = restaurantRepository.findById("r1");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Pizza Hut");
        assertThat(found.get().getDishes()).hasSize(1);
    }

    @Test
    void testRestaurantRepositoryDelete() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId("r1");
        restaurant.setName("Pizza Hut");
        restaurantRepository.save(restaurant);

        restaurantRepository.deleteById("r1");
        assertThat(restaurantRepository.findById("r1")).isEmpty();
    }
}
