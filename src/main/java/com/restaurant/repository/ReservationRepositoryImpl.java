package com.restaurant.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.restaurant.dto.findReDto;
import com.restaurant.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.awt.*;

@Repository
@Transactional
public class ReservationRepositoryImpl {

    JPAQueryFactory query;

    public ReservationRepositoryImpl (EntityManager em){
        this.query = new JPAQueryFactory(em);
    }
    int pageNumber = 1; // 가져올 페이지 번호
    int pageSize = 10; // 한 페이지에 보여줄 레코드 개수

    PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);



    private final QReservation qReservation = QReservation.reservation;
    private final QRest qRestaurant = QRest.rest;

    private final QRestImg qRestImg = QRestImg.restImg;

    //식당조회
    public Rest findRestaurant(Long rsId){
        Rest rest = query.select(qRestaurant).from(qRestaurant).where(qRestaurant.id.eq(rsId)).fetchOne();
        return rest;
    }


    //예약조회
    public Page<findReDto> findReservations(int memberId , Pageable pageable) {
        QueryResults<findReDto> queryResults = query.select(Projections.constructor(findReDto.class, qReservation.re_id,qReservation.create_date, qReservation.people, qReservation.request, qRestaurant.restNm))
                .from(qReservation)
                .join(qRestaurant)
                .where(qReservation.mId.eq(memberId).and(qReservation.reservation_status.eq(1))).orderBy(qReservation.create_date.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable,queryResults.getTotal());
    }

    //예약취소
    public int statusReservation(int re_id){
        long updatedRows = query.update(qReservation)
                .set(qReservation.reservation_status, 0)
                .where(qReservation.re_id.eq(re_id))
                .execute();
        int result = updatedRows > 0 ? 0 : 1;
        return result;
    }
    /*
    public RestImg findImage(Restaurant rsId){
        RestImg image = query.select(qRestImg).from(qRestImg).where(qRestImg.id.eq(1L).and(qRestImg.repimgYn.eq("Y"))).fetchOne();
        return image;
    }

     */


}
