package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
 
    private int c_id;
    private int m_id;
    private int p_id;
    private String comment;
    private Date create_date;
    private Date modify_date;
    private Date delete_date;
    private Date is_delete;

}


