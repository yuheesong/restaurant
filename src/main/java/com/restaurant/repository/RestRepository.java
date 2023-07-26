package com.restaurant.repository;

import com.restaurant.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RestRepository extends JpaRepository<Rest, Long>, QuerydslPredicateExecutor<Rest> {

    List<Rest> findByRestNm(String restNm);

    List<Rest> findByRestNmOrRestDetail(String restNm, String restDetail);

}
