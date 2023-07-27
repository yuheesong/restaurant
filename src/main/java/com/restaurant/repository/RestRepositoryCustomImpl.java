package com.restaurant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.QRest;
import com.restaurant.entity.Rest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class RestRepositoryCustomImpl implements RestRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public RestRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }*/

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QRest.rest.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("restNm", searchBy)){
            return QRest.rest.restNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QRest.rest.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Rest> getAdminRestPage(RestSearchDto restSearchDto, Pageable pageable) {

        List<Rest> content = queryFactory
                .selectFrom(QRest.rest)
                .where(regDtsAfter(restSearchDto.getSearchDateType()),
                        //searchSellStatusEq(restSearchDto.getSearchSellStatus()),
                        searchByLike(restSearchDto.getSearchBy(),
                                restSearchDto.getSearchQuery()))
                .orderBy(QRest.rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QRest.rest)
                .where(regDtsAfter(restSearchDto.getSearchDateType()),
                        //searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(restSearchDto.getSearchBy(), restSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression restNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null :
                QRest.rest.restNm.like("%" + searchQuery + "%");
    }
}
