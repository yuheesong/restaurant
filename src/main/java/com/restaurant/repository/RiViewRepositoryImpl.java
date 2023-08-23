package com.restaurant.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Repository
@Transactional
public class RiViewRepositoryImpl {

    JPAQueryFactory query;

    public RiViewRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }





}
