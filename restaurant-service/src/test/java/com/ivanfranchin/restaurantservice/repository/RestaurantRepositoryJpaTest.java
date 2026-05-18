package com.ivanfranchin.restaurantservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivanfranchin.restaurantservice.PostgreSQLTestcontainers;
import com.ivanfranchin.restaurantservice.model.Dish;
import com.ivanfranchin.restaurantservice.model.Restaurant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@DataJpaTest
@ImportTestcontainers(PostgreSQLTestcontainers.class)
class RestaurantRepositoryJpaTest {

  @Autowired private RestaurantRepository restaurantRepository;

  @Test
  void testSaveAndFindById() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");

    Dish dish = new Dish();
    dish.setId("d1");
    dish.setName("Pizza Margherita");
    dish.setPrice(new BigDecimal("6.99"));
    dish.setRestaurant(restaurant);

    restaurant.setDishes(Set.of(dish));
    restaurantRepository.save(restaurant);

    Optional<Restaurant> found = restaurantRepository.findById("r1");
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Pizza Hut");
    assertThat(found.get().getDishes()).hasSize(1);
    assertThat(found.get().getDishes().iterator().next().getName()).isEqualTo("Pizza Margherita");
  }

  @Test
  void testFindAll() {
    Restaurant r1 = new Restaurant();
    r1.setId("r1");
    r1.setName("Pizza Hut");

    Restaurant r2 = new Restaurant();
    r2.setId("r2");
    r2.setName("McDonald's");

    restaurantRepository.saveAll(List.of(r1, r2));

    List<Restaurant> restaurants = restaurantRepository.findAll();
    assertThat(restaurants).hasSize(2);
  }

  @Test
  void testDelete() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");
    restaurantRepository.save(restaurant);

    restaurantRepository.deleteById("r1");
    assertThat(restaurantRepository.findById("r1")).isEmpty();
  }

  @Test
  void testSaveRestaurantWithMultipleDishes() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");

    Dish d1 = new Dish();
    d1.setId("d1");
    d1.setName("Pizza Margherita");
    d1.setPrice(new BigDecimal("6.99"));
    d1.setRestaurant(restaurant);

    Dish d2 = new Dish();
    d2.setId("d2");
    d2.setName("Coke");
    d2.setPrice(new BigDecimal("2.50"));
    d2.setRestaurant(restaurant);

    restaurant.setDishes(Set.of(d1, d2));
    restaurantRepository.save(restaurant);

    Restaurant found = restaurantRepository.findById("r1").orElseThrow();
    assertThat(found.getDishes()).hasSize(2);
  }

  @Test
  void testCascadeDeleteRemovesDishes() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId("r1");
    restaurant.setName("Pizza Hut");

    Dish dish = new Dish();
    dish.setId("d1");
    dish.setName("Pizza Margherita");
    dish.setPrice(new BigDecimal("6.99"));
    dish.setRestaurant(restaurant);
    restaurant.setDishes(Set.of(dish));

    restaurantRepository.save(restaurant);
    restaurantRepository.deleteById("r1");

    assertThat(restaurantRepository.findById("r1")).isEmpty();
  }
}
