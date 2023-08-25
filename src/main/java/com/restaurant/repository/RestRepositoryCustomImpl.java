package com.restaurant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restaurant.dto.MainRestDto;
import com.restaurant.dto.QMainRestDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.QRest;
import com.restaurant.entity.QRestImg;
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
    @Override
    public Page<MainRestDto> getMainRestPage(RestSearchDto restSearchDto, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(restNmLike(restSearchDto.getSearchQuery()))
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(restNmLike(restSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> getCategoryRestPage(String category, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression categoryCondition = rest.category.eq(category);

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                //.where(restNmLike(restSearchDto.getSearchQuery()))
                .where(categoryCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(categoryCondition)
                //.where(restNmLike(restSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithSeoul(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression seoulCondition = rest.address.startsWith("서울 ");

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = seoulCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> getRegionRestPage(String region, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression regionCondition = rest.region.eq(region);

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                //.where(restNmLike(restSearchDto.getSearchQuery()))
                .where(regionCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(regionCondition)
                //.where(restNmLike(restSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainRestDto> findByAddressStartingWithGyeongin(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression gyeonggiCondition = rest.address.startsWith("경기 ");
        BooleanExpression incheonCondition = rest.address.startsWith("인천 ");
        BooleanExpression gyeonginCondition = gyeonggiCondition.or(incheonCondition);

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = gyeonginCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithGangwon(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression gangwonCondition = rest.address.startsWith("강원특별자치도 ");

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = gangwonCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithChungcheong(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression chungnamCondition = rest.address.startsWith("충남 ");
        BooleanExpression chungbukCondition = rest.address.startsWith("충북 ");
        BooleanExpression chungcheongCondition = chungnamCondition.or(chungbukCondition);

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = chungcheongCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithDaejeon(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression daejeonCondition = rest.address.startsWith("대전 ");
        BooleanExpression sejongCondition = rest.address.startsWith("세종특별자치시 ");
        BooleanExpression daejeonsejongCondition = daejeonCondition.or(sejongCondition);

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = daejeonsejongCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithJeolla(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression jeonnamCondition = rest.address.startsWith("전남 ");
        BooleanExpression jeonbukCondition = rest.address.startsWith("전북 ");
        BooleanExpression gwangjuCondition = rest.address.startsWith("광주 ");
        BooleanExpression jeollaCondition = jeonnamCondition.or(jeonbukCondition).or(gwangjuCondition);

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = jeollaCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithGyeongsang(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression gyeongnamCondition = rest.address.startsWith("경남 ");
        BooleanExpression gyeongbukCondition = rest.address.startsWith("경북 ");
        BooleanExpression gyeongsangCondition = gyeongnamCondition.or(gyeongbukCondition);

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = gyeongsangCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithJeju(List<String> regions, Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression jejuCondition = rest.address.startsWith("제주특별자치도 ");

        BooleanExpression regionCondition = null;
        if (regions != null && !regions.isEmpty()) {
            for (String region : regions) {
                BooleanExpression currentCondition = rest.region.eq(region);
                if (regionCondition == null) {
                    regionCondition = currentCondition;
                } else {
                    regionCondition = regionCondition.or(currentCondition);
                }
            }
        }

        BooleanExpression combinedCondition = jejuCondition;
        if (regionCondition != null) {
            combinedCondition = combinedCondition.and(regionCondition);
        }

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(combinedCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithNamdong(Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression busanCondition = rest.address.startsWith("부산 ");
        BooleanExpression ulsanCondition = rest.address.startsWith("울산 ");
        BooleanExpression daeguCondition = rest.address.startsWith("대구 ");
        BooleanExpression namdongCondition = busanCondition.or(ulsanCondition).or(daeguCondition);

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(namdongCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(namdongCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithBusan(Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression busanCondition = rest.address.startsWith("부산 ");

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(busanCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(busanCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithUlsan(Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression ulsanCondition = rest.address.startsWith("울산 ");

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(ulsanCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(ulsanCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainRestDto> findByAddressStartingWithDaegu(Pageable pageable) {
        QRest rest = QRest.rest;
        QRestImg restImg = QRestImg.restImg;

        BooleanExpression daeguCondition = rest.address.startsWith("대구 ");

        List<MainRestDto> content = queryFactory
                .select(
                        new QMainRestDto(
                                rest.id,
                                rest.restNm,
                                rest.restPhone,
                                rest.address,
                                rest.category,
                                rest.introduction,
                                rest.restDetail,
                                rest.region,
                                restImg.imgUrl)
                )
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(daeguCondition)
                .orderBy(rest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(restImg)
                .join(restImg.rest, rest)
                .where(restImg.repimgYn.eq("Y"))
                .where(daeguCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}