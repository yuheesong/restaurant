package com.restaurant.dto;

import lombok.Data;

@Data
public class ReservationFormDto {

    //시간조회dto

    private Long id;
    private int year;
    private int month;
    private int day;
    private int hour;

    private String count;
    private String inputValue;
}
