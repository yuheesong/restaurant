package com.restaurant.repository;

import com.restaurant.entity.RestImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestImgRepository extends JpaRepository<RestImg, Long> {
    List<RestImg> findByRestIdOrderByIdAsc(Long restId);
}
