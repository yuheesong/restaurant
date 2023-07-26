package com.restaurant.repository;

import com.restaurant.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestRepository extends JpaRepository<Rest, Long> {

    List<Rest> findByRestNm(String restNm);

    List<Rest> findByRestNmOrRestDetail(String restNm, String restDetail);

}
