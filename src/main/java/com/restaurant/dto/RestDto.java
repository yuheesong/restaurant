package com.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestDto {
    private Long id;

    private String restNm;

    private String restPhone;

    private String address;

    private String introduction;

    private String restDetail;

    private String category;
}
