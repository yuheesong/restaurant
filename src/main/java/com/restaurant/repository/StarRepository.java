package com.restaurant.repository;

import com.restaurant.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star,Long> {
}
