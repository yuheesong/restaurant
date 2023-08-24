package com.restaurant.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int re_id;
    @Column(name = "m_id")
    private int mId;
    @Column(name = "rs_id")
    private int rsId;

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "m_id")
    private Member re_member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Rest re_restaurant;

    private String request;

    private String people;

    private Long review;
    private Date create_date;
    private int reservation_status;


}
