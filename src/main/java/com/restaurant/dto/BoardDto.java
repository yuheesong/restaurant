package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private int p_id;
    private int m_id;
    private String title;
    private String contents;
    private Date create_date;
    private Date modify_date;
    private Date delete_date;
    private Date is_delete;

}
