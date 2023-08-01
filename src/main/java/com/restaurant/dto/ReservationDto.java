package com.restaurant.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDto {

    private int re_id;
    private int m_id;
    private int rs_id;
    private String request;
    private String people;
    private Date create_date;
    private String role;


}
