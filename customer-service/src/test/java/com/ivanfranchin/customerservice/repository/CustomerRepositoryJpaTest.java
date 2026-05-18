package com.ivanfranchin.customerservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivanfranchin.customerservice.MySQLTestcontainers;
import com.ivanfranchin.customerservice.model.Customer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@DataJpaTest
@ImportTestcontainers(MySQLTestcontainers.class)
class CustomerRepositoryJpaTest {

  @Autowired private CustomerRepository customerRepository;

  @Test
  void testSaveAndFindById() {
    Customer customer = new Customer();
    customer.setId("c1");
    customer.setName("John");
    customer.setAddress("123 Main St");
    customerRepository.save(customer);

    Optional<Customer> found = customerRepository.findById("c1");
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("John");
    assertThat(found.get().getAddress()).isEqualTo("123 Main St");
  }

  @Test
  void testFindAll() {
    Customer c1 = new Customer();
    c1.setId("c1");
    c1.setName("John");
    c1.setAddress("123 Main St");

    Customer c2 = new Customer();
    c2.setId("c2");
    c2.setName("Jane");
    c2.setAddress("456 Oak Ave");

    customerRepository.saveAll(List.of(c1, c2));

    List<Customer> customers = customerRepository.findAll();
    assertThat(customers).hasSize(2);
  }

  @Test
  void testDelete() {
    Customer customer = new Customer();
    customer.setId("c1");
    customer.setName("John");
    customer.setAddress("123 Main St");
    customerRepository.save(customer);

    customerRepository.deleteById("c1");
    assertThat(customerRepository.findById("c1")).isEmpty();
  }

  @Test
  void testFindById_whenNotFound_returnsEmpty() {
    Optional<Customer> found = customerRepository.findById("nonexistent");
    assertThat(found).isEmpty();
  }
}
