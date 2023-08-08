package com.restaurant.entity;


import com.restaurant.constant.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int p_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member m_id;

    private String title;
    private String contents;
    private Role role;

    private int view;
    private Date create_date;
    private Date modify_date;
    private Date delete_date;



}
