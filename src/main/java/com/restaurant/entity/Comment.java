package com.restaurant.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int c_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member m_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board p_id;
    private String comment;
    private Date create_date;
    private Date modify_date;
    private Date delete_date;


}


