package com.restaurant.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restaurant.constant.Role;
import com.restaurant.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;


@Repository
@Transactional
public class StarRepositoryImpl {

    JPAQueryFactory query;

    public StarRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }


    private final QStar qStar = QStar.star;

    //공지사항 전체조회
    public Page<Star> findStar(Member member,Pageable pageable){
        QueryResults<Star> list = query.selectFrom(qStar).where(qStar.member.eq(member))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(list.getResults(),pageable,list.getTotal());
    }

}
