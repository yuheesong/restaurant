package com.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestSearchDto {
    private String searchDateType;

    private String searchBy;
    private String searchQuery = "";
    private String category;
}
