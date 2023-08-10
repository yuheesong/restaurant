package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class findReDto {

    int re_id;
    Date create_date;
    String people;
    String request;
    String  restNm;
}
