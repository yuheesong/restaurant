package com.restaurant.repository;


import com.restaurant.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Rest, Integer> {
}
