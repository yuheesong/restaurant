package com.restaurant.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.restaurant.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Repository
@Transactional
public class ReViewRepositoryImpl {

    JPAQueryFactory query;

    public ReViewRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);

    }


    QReservation qReservation =QReservation.reservation;

    public Double findRiView(Long id){
        Double Double = query.select(qReservation.review.sum().divide(qReservation.review.count()).doubleValue())
                .from(qReservation)
                .where(qReservation.re_restaurant.id.eq(id))
                .groupBy(qReservation.re_restaurant.id)
                .fetchOne();
        return Double;
    }
    public long save(int re_id,Long i){
        long execute = query.update(qReservation).set(qReservation.review, i).where(qReservation.re_id.eq(re_id)).execute();
        return execute;
    }

}
