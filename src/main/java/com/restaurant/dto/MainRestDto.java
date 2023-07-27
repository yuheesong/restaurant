package com.restaurant.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainRestDto {
    private Long id;
    private String restNm;
    private String restPhone;
    private String address;
    private String category;
    private String introduction;
    private String restDetail;
    private String imgUrl;

    @QueryProjection
    public MainRestDto(Long id, String restNm, String restPhone, String address, String category,
                       String introduction, String restDetail, String imgUrl){
        this.id = id;
        this.restNm = restNm;
        this.restPhone = restPhone;
        this.address = address;
        this.category = category;
        this.introduction = introduction;
        this.restDetail = restDetail;
        this.imgUrl = imgUrl;
    }
}
